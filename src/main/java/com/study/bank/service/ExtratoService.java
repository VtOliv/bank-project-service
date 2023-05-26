package com.study.bank.service;

import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.study.bank.domain.Extrato;
import com.study.bank.domain.Operacao;
import com.study.bank.domain.filter.ExtratoFilter;
import com.study.bank.repository.ExtratoRepository;
import com.study.bank.util.Conditions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExtratoService extends Conditions<Extrato> {

	private final ExtratoRepository repo;

	private final Conditions<Extrato> conditions;

	public Extrato gerarExtrato(Integer numconta, Double saldoInicial, Double saldoFinal, Operacao op) {
		var extrato = new Extrato();

		extrato.setNumconta(numconta);
		extrato.setData(LocalDateTime.now());
		extrato.setOperacao(op.getSigla());
		extrato.setSaldo(saldoFinal);

		switch (op.getCode()) {
		case 1 -> extrato.setMovimentacao(saldoInicial - saldoFinal);
		case 2 -> {
			var dif = saldoFinal - saldoInicial;
			extrato.setMovimentacao(dif);
		}
		case 3 -> extrato.setMovimentacao(saldoInicial - saldoFinal);

		}

		return repo.save(extrato);
	}

	public Page<Extrato> buscarExtrato(ExtratoFilter filter, Pageable pageable) {
		if (filter.getNumconta() == null && filter.getOperacao() == null) {
			return repo.findAll(pageable);
		} else {

			return repo.findAll(where(conditions.equals("numconta", filter.getNumconta())
					.and(conditions.likeIgnoreCase("operacao", filter.getOperacao()))), pageable);
		}
	}
}
