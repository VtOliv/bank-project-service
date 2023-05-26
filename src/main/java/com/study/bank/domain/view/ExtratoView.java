package com.study.bank.domain.view;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtratoView {

	private String operationId;
	
	private Integer numConta;
	
	private Double saldo;
	
	private Double movimentacao;
	
	private String operacao;
	
	private LocalDateTime data;
}
