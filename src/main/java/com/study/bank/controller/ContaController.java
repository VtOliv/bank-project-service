package com.study.bank.controller;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.bank.domain.form.ContaForm;
import com.study.bank.domain.view.ContaView;
import com.study.bank.domain.view.TransacoesView;
import com.study.bank.service.ContaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ContaController {

	private final ContaService service;

	@PostMapping("/criar")
	public ResponseEntity<ContaView> criarConta(@RequestBody ContaForm form) {

		log.info("Criando conta: NumConta: {}", form.getDono());

		var conta = service.criarConta(form);

		log.info("Conta criada: NumConta: {} Dono: {}", conta.getNumConta(), conta.getDono());

		return ResponseEntity.status(OK).body(conta);

	}

	@GetMapping("/buscar")
	public ResponseEntity<ContaView> buscarConta(Integer numconta) {
		log.info("Buscando conta: NumConta:{}", numconta);

		var conta = service.buscarConta(numconta);

		return ResponseEntity.status(OK).body(conta);
	}

	@PutMapping("/alterar/{numconta}")
	public ResponseEntity<ContaView> alterarConta(@PathVariable Integer numconta, @RequestBody ContaForm form) {

		log.info("Alterar conta: Tipo: {} Dono: {}", form.getTipo(), form.getDono());

		var conta = service.alterarConta(form, numconta);
		
		log.info("Conta alterada: Tipo: {} Dono: {}", conta.getTipo(), conta.getDono());

		return ResponseEntity.status(OK).body(conta);
	}
	
	@PutMapping("/sacar/{numconta}/{valorDoSaque}")
	public ResponseEntity<TransacoesView> sacar(@PathVariable Integer numconta, @PathVariable Double valorDoSaque) {

		log.info("Saque: Valor: {} NumConta: {}", valorDoSaque, numconta);

		var transacao = service.sacar(valorDoSaque, numconta);
		
		log.info("Novo saldo: Saldo: {}", transacao.getSaldo());

		return ResponseEntity.status(OK).body(transacao);
	}
	
	@PutMapping("/depositar/{numconta}/{valorDoDeposito}")
	public ResponseEntity<TransacoesView> depositar(@PathVariable Integer numconta, @PathVariable Double valorDoDeposito) {

		log.info("Depósito: Valor: {} NumConta: {}", valorDoDeposito, numconta);

		var transacao = service.depositar(valorDoDeposito, numconta);
		
		log.info("Novo saldo: Saldo: {}", transacao.getSaldo());

		return ResponseEntity.status(OK).body(transacao);
	}
	
	@PutMapping("/pagarConta/{numconta}/{valorDoPagamento}")
	public ResponseEntity<TransacoesView> pagarConta(@PathVariable Integer numconta, @PathVariable Double valorDoPagamento) {

		log.info("Pagamento: Valor: {} NumConta: {}", valorDoPagamento, numconta);

		var transacao = service.pagarConta(valorDoPagamento, numconta);
		
		log.info("Novo saldo: Saldo: {}", transacao.getSaldo());

		return ResponseEntity.status(OK).body(transacao);
	}
	
	@PutMapping("/fecharConta")
	public ResponseEntity<TransacoesView> fecharConta(Integer numconta) {

		log.info("Encerrar conta: NumConta: {}", numconta);

		var transacao = service.fecharConta(numconta);
		
		var status = transacao.getSaldo().doubleValue() > 0? "Ativo" : "Desativado";
		
		log.info("Status: {}", status);

		return ResponseEntity.status(OK).body(transacao);
	}
}
