package com.study.bank.service;

import static com.study.bank.domain.TipoConta.getContaTipoPorCodigo;
import static com.study.bank.domain.TipoConta.getContaTipoPorSigla;
import static java.lang.System.out;

import java.sql.Connection;
import java.util.Scanner;

import com.study.bank.domain.Conta;

public class ContaService {

	public static Scanner scan = new Scanner(System.in);

	private Conta modelo(Conta conta) {

		out.println("Digite o tipo da conta [cp - Conta poupança / cc - Conta corrente] :");
		conta.setTipo(getContaTipoPorSigla(scan.nextLine()).orElse(null));

		out.println("Digite o nome do titular da conta: ");
		conta.setDono(scan.next());

		conta.setStatus(false);
		conta.setSaldo(0.0);

		return conta;
	}

	public Conta criarConta(Connection conn) {

		var conta = modelo(new Conta());

		try {
			var preparedStatement = conn
					.prepareStatement("insert into conta(tipo, dono, saldo, status) values(?, ?, ?, ?)");
			preparedStatement.setString(1, conta.getTipo().getSigla());
			preparedStatement.setString(2, conta.getDono());
			preparedStatement.setDouble(3, conta.getSaldo());
			preparedStatement.setBoolean(4, conta.isStatus());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			out.println(e);
		}
		return conta;
	}

	public Conta buscarConta(String message, Connection conn) {

		out.println(message);
		var numConta = scan.nextInt();

		Conta conta = null;
		try {
			var preparedStatement = conn.prepareStatement("select * from conta where numConta = ?");
			preparedStatement.setInt(1, numConta);
			var resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				conta = new Conta();
				conta.setNumConta(resultSet.getInt("numConta"));
				conta.setTipo(getContaTipoPorSigla(resultSet.getString("tipo")).get());
				conta.setDono(resultSet.getString("dono"));
				conta.setSaldo(resultSet.getDouble("saldo"));
				conta.setStatus(resultSet.getBoolean("status"));
			}
		} catch (Exception e) {
			conta = null;
		}
		return conta;
	}

	public void alterarConta(Conta conta, Connection conn) {

		out.println("Escolha a opção desejada:");
		out.println("[1] - Nome do dono");
		out.println("[2] - Tipo da conta");

		var opt = scan.nextInt();

		switch (opt) {
		case 1 -> {
			out.println("Digite o nome do titular da conta");
			conta.setDono(scan.nextLine());
		}
		case 2 -> {
			out.println("Digite o tipo da conta [1 - Conta poupança / 2 - Conta corrente] ");
			conta.setTipo(getContaTipoPorCodigo(scan.nextInt()).orElse(null));
		}
		}

		try {
			var preparedStatement = conn.prepareStatement("update conta set tipo = ?, dono = ? where numConta = ?");
			preparedStatement.setString(1, conta.getTipo().getSigla());
			preparedStatement.setString(2, conta.getDono());
			preparedStatement.setInt(3, conta.getNumConta());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			out.println(e);
		}

		out.println("Informações atualizadas com sucesso");
		mostrarInfoDaConta(conta);
	}

	public void mostrarInfoDaConta(Conta acc) {
		var status = acc.isStatus() ? "Ativa" : "Desativada";

		out.println("------------------- INFO DA CONTA ------------------");
		out.println("Numero da conta: " + acc.getNumConta());
		out.println("Tipo da Conta: " + acc.getTipo().getDescricao());
		out.println("Nome do Titular: " + acc.getDono());
		out.println("Saldo da Conta: " + acc.getSaldo());
		out.println("Status da Conta: " + status);
		out.println("----------------------------------------------------");
		out.println("");
	}

	public void menu() {
		out.println("""
				-------------------- Bank System -------------------
				Escolha a opcao desejada:
				[1] - Buscar outra conta
				[2] - Criar conta
				[3] - Alterar dados da conta
				[4] - Mostrar dados da conta
				[5] - Ativar conta
				[6] - Desativar conta
				[7] - Sacar
				[8] - Depositar
				[9] - Cobrar mensalidade
				[0] - Encerrar sistema
				----------------------------------------------------
						""");
	}

	public static Double inserirValor(String message) {
		out.println(message);
		var value = scan.nextDouble();

		return value;
	}
}
