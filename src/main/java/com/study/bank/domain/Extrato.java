package com.study.bank.domain;

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
@Entity
@Table(name = "extrato")
public class Extrato {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String operationId;
	
    @Column(name = "numconta") 
	private Integer numconta;
	
	@Column(name = "saldo")
	private Double saldo;
	
	@Column(name = "movimentacao")
	private Double movimentacao;
	
	@Column(name = "operacao")
	private String operacao;
	
	@Column(name = "data")
	private LocalDateTime data;
}
