package com.study.bank.configuration;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.study.bank.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControlExceptionHandler {

	public static final String X_RD_TRACEID = "X-rd-traceid";
	public static final String CONSTRAINT_VALIDATION_FAILED = "Constraint validation failed";

	@ExceptionHandler(value = { BusinessException.class })
	protected ResponseEntity<Object> handleConflict(BusinessException ex, WebRequest request) {
		log.warn("error={} message={}", ex.getHttpStatusCode().value(), ex.getDescription());
		return getException(ex.getHttpStatusCode(), ex.getMessage(), ex.getDescription());
	}

	@ExceptionHandler({ Throwable.class })
	public ResponseEntity<Object> handleException(Throwable eThrowable) {
		log.warn("error=500 message={}", eThrowable);
		return getException(INTERNAL_SERVER_ERROR, ofNullable(eThrowable).map(Throwable::toString).orElse(null), null);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exMethod,
			WebRequest request) {
		var error = exMethod.getName() + " should be " + exMethod.getRequiredType().getName();

		return getException(BAD_REQUEST, CONSTRAINT_VALIDATION_FAILED, error);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exMethod, WebRequest request) {
		var errors = new ArrayList<>();

		exMethod.getConstraintViolations().stream().forEach(violation -> {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		});

		return getException(BAD_REQUEST, CONSTRAINT_VALIDATION_FAILED, errors.toString());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationError(MethodArgumentNotValidException exMethod) {
		var fieldErrorDtos = exMethod.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField().concat(":").concat(f.getDefaultMessage())).map(String::new)
				.collect(Collectors.toList());

		return getException(BAD_REQUEST, CONSTRAINT_VALIDATION_FAILED, fieldErrorDtos.toString());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> validationError(HttpMessageNotReadableException exMethod) {
		return getException(BAD_REQUEST, CONSTRAINT_VALIDATION_FAILED, exMethod.getMostSpecificCause().getMessage());
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<Object> handleException(MissingServletRequestParameterException e) {
		return getException(BAD_REQUEST, ofNullable(e.getMessage()).orElse(e.toString()), null);
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
		return getException(METHOD_NOT_ALLOWED, ofNullable(e.getMessage()).orElse(e.toString()), null);
	}

	@ExceptionHandler({ BindException.class })
	public ResponseEntity<Object> handleException(BindException e) {
		var fieldErrors = e.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField().concat(":").concat(f.getDefaultMessage())).map(String::new)
				.collect(Collectors.toList());

		return getException(BAD_REQUEST, CONSTRAINT_VALIDATION_FAILED, fieldErrors.toString());
	}

	@ExceptionHandler({ NumberFormatException.class })
	public ResponseEntity<Object> handleException(NumberFormatException e) {
		return getException(BAD_REQUEST, ofNullable(e.getMessage()).orElse(e.toString()), null);
	}

	private ResponseEntity<Object> getException(HttpStatus httpStatus, String message, String description) {
		var ex = BusinessException.builder().httpStatusCode(httpStatus).message(message).description(description)
				.build();

		var responseHeaders = new HttpHeaders();
		responseHeaders.set(X_RD_TRACEID, getTraceID());

		return ResponseEntity.status(ex.getHttpStatusCode()).headers(responseHeaders).body(ex.getOnlyBody());
	}

	private String getTraceID() {
		return ofNullable(MDC.get("")).orElse("not available");
	}
}