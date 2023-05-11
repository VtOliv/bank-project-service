package com.study.bank.domain.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtratoFilter {

	private Integer numconta;
	
	private String operacao;

}
