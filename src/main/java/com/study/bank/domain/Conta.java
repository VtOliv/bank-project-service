package com.study.bank.domain;

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
@Entity
@Table(name = "conta")
public class Conta {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numconta") 
	private Integer numConta;
	
	@Column(name = "tipo")
	private TipoConta tipo;
	
	@Column(name = "dono")
	private String dono;
	
	@Column(name = "saldo")
	private Double saldo;
	
	@Column(name = "status")
	private boolean status;
}