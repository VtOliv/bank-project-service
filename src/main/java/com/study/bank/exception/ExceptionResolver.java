package com.study.bank.exception;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

public class ExceptionResolver {
	public static String getRootException(Throwable ex) {
		return String.format("%s in class: %s Line: %s", getRootCauseMessage(ex),
				getRootCause(ex).getStackTrace()[0].getClassName(),
				getRootCause(ex).getStackTrace()[0].getLineNumber());
	}
}