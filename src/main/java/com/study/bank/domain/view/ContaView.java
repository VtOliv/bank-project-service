package com.study.bank.domain.view;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaView {

	private Integer numConta;
	
	private String tipo;
	
	private String dono;
	
	private BigDecimal saldo;
	
	private String status;
}
