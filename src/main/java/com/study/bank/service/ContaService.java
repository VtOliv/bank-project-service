package com.study.bank.service;

import static com.study.bank.domain.TipoConta.CC;
import static com.study.bank.domain.TipoConta.CP;
import static com.study.bank.domain.TipoConta.getContaTipoPorCodigo;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Optional.ofNullable;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.study.bank.domain.Conta;
import com.study.bank.domain.TipoConta;
import com.study.bank.domain.form.ContaForm;
import com.study.bank.domain.form.TransacaoForm;
import com.study.bank.domain.view.ContaView;
import com.study.bank.domain.view.TransacoesView;
import com.study.bank.repository.ContaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {

	private final ContaRepository repo;

	private ContaView viewBuilder(Conta conta) {
		var saldo = new BigDecimal(conta.getSaldo()).setScale(2, HALF_UP);
		var status = conta.isStatus() ? "Ativo" : "Desativado";

		return ContaView.builder().numConta(conta.getNumConta()).dono(conta.getDono())
				.tipo(conta.getTipo().getDescricao()).saldo(saldo).status(status).build();
	}

	public ContaView criarConta(ContaForm form) {

		var conta = new Conta();

		conta.setDono(form.getDono());
		conta.setTipo(TipoConta.getContaTipoPorCodigo(form.getTipo()).orElse(CC));
		conta.setSaldo(conta.getTipo().equals(CC) ? 50.00 : 150.00);
		conta.setStatus(true);

		repo.save(conta);

		return viewBuilder(conta);
	}

	public ContaView buscarConta(Integer numconta) {

		var conta = repo.findById(numconta).orElseThrow();

		return viewBuilder(conta);
	}

	public ContaView alterarConta(ContaForm form, Integer numconta) {

		var conta = repo.findById(numconta).orElseThrow();

		conta.setDono(ofNullable(form.getDono()).orElse(conta.getDono()));
		conta.setTipo(getContaTipoPorCodigo(ofNullable(form.getTipo()).orElse(conta.getTipo().getCode())).get());

		var saved = repo.save(conta);

		return viewBuilder(saved);

	}

	public TransacoesView depositar(TransacaoForm form) {

		var conta = repo.findById(form.getNumconta()).orElseThrow();
		var view = new TransacoesView();

		if (conta.isStatus()) {
			conta.setSaldo(conta.getSaldo() + form.getValor());

			repo.save(conta);
			view.setMessage("Você depositou: R$ " + form.getValor());
		} else {
			view.setMessage("A conta não está ativa !");
		}
		view.setSaldo(new BigDecimal(conta.getSaldo()).setScale(2, HALF_UP));

		return view;
	}

	public TransacoesView sacar(TransacaoForm form) {

		var conta = repo.findById(form.getNumconta()).orElseThrow();
		var view = new TransacoesView();

		if (!conta.isStatus()) {
			view.setMessage("A conta não está ativa !");
		} else {

			if (conta.getSaldo() >= form.getValor()) {
				conta.setSaldo(conta.getSaldo() - form.getValor());
				view.setMessage("Voce sacou: R$ " + form.getValor());

				repo.save(conta);
			} else {
				view.setMessage("Voce nao possui saldo suficiente");
			}
		}
		view.setSaldo(new BigDecimal(conta.getSaldo()).setScale(2, HALF_UP));
		return view;
	}

	public TransacoesView fecharConta(Integer numconta) {

		var conta = repo.findById(numconta).orElseThrow();
		var view = new TransacoesView();

		if (conta.getSaldo() > 0) {
			view.setMessage("Conta possui saldo e nao pode ser fechada");
			view.setSaldo(new BigDecimal(conta.getSaldo()).setScale(2, HALF_UP));
		} else {
			conta.setStatus(false);

			view.setMessage("A conta foi desativada");
			view.setSaldo(ZERO);

			repo.save(conta);
		}

		return view;
	}

	public TransacoesView pagarConta(TransacaoForm form) {
		var contaComTaxaCC = form.getValor() + 12.00;
		var contaComTaxaCP = form.getValor() + 20.00;

		var conta = repo.findById(form.getNumconta()).orElseThrow();
		var view = new TransacoesView();

		if (CC.equals(conta.getTipo()) && conta.getSaldo() >= contaComTaxaCC) {

			conta.setSaldo(conta.getSaldo() - contaComTaxaCC);
			view.setMessage("Foi paga a conta de: R$ " + form.getValor() + " e cobrada a taxa de: R$ " + 12.00);
			repo.save(conta);
		} else if (CP.equals(conta.getTipo()) && conta.getSaldo() >= contaComTaxaCP) {

			conta.setSaldo(conta.getSaldo() - contaComTaxaCP);
			view.setMessage("Foi paga a conta de: R$ " + form.getValor() + " e cobrada a taxa de: R$ " + 20.00);
			repo.save(conta);
		} else {
			view.setMessage("Voce nao possui saldo suficiente");
		}

		view.setSaldo(new BigDecimal(conta.getSaldo()).setScale(2, HALF_UP));

		return view;
	}
}