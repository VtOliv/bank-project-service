package com.study.bank.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenhaForm {
	
	private Integer numConta;
	private String senhaNova;
	private String senhaAntiga;
	private Boolean isLogado;
}
