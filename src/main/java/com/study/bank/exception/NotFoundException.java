package com.study.bank.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
	private static final long serialVersionUID = -4077623339671616919L;
	
	public NotFoundException() {
		super.setHttpStatusCode(HttpStatus.NOT_FOUND);
		super.setMessage("Não foi encontrada nenhuma conta com esse número");
	}
}
