package com.study.bank.exception;

import org.springframework.http.HttpStatus;

public class CloseAccountException extends BusinessException {
	private static final long serialVersionUID = -4077623339671616919L;
	
	public CloseAccountException() {
		super.setHttpStatusCode(HttpStatus.BAD_REQUEST);
		super.setMessage("Não foi possível encerrar a conta pois saldo é maior que 0");
	}
}
