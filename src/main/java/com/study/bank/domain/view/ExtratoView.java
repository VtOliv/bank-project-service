package com.study.bank.domain.view;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
