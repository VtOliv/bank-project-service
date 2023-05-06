package com.study.bank.domain;

import static com.study.bank.domain.TipoConta.CC;
import static com.study.bank.domain.TipoConta.CP;
import static java.lang.System.out;

import java.sql.Connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

	private Integer numConta;
	private TipoConta tipo;
	private String dono;
	private Double saldo;
	private boolean status;

	public void abrirConta(Connection conn) {
		this.status = true;

		this.saldo = tipo.equals(CC) ? 50.00 : 150.00;

		try {
			var preparedStatement = conn.prepareStatement("update conta set saldo = ?, status = ? where numConta = ?");
			preparedStatement.setDouble(1, this.saldo);
			preparedStatement.setBoolean(2, this.status);
			preparedStatement.setInt(3, this.numConta);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			out.println(e);
		}

		out.println("A conta foi ativada com sucesso e possui R$ " + this.saldo + " de saldo");
	}

	public void fecharConta(Connection conn) {

		if (this.saldo > 0) {
			out.println("Conta possui saldo e nao pode ser fechada");
			out.println("");
		} else {
			this.status = false;

			try {
				var preparedStatement = conn.prepareStatement("update conta set status = ? where numConta = ?");
				preparedStatement.setBoolean(1, this.status);
				preparedStatement.setInt(2, this.numConta);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				out.println(e);
			}

			out.println("A conta foi desativada");
		}
	}

	public void depositar(Double valorDoDeposito, Connection conn) {

		if (this.status) {
			this.saldo += valorDoDeposito;

			try {
				var preparedStatement = conn.prepareStatement("update conta set saldo = ? where numConta = ?");
				preparedStatement.setDouble(1, this.saldo);
				preparedStatement.setInt(2, this.numConta);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				out.println(e);
			}

			out.println("Voce depositou: " + valorDoDeposito + "R$");
			out.println("Seu novo saldo: " + this.saldo + "R$");
			out.println("");
		} else {
			out.println("A conta não está ativa !");
		}
	}

	public void sacar(Double valorDoSaque, Connection conn) {

		if (!this.status) {
			out.println("A conta não está ativa !");
			return;
		}

		if (this.saldo >= valorDoSaque) {
			this.saldo -= valorDoSaque;
			out.println("Voce sacou: " + valorDoSaque + "R$");

			try {
				var preparedStatement = conn.prepareStatement("update conta set saldo = ? where numConta = ?");
				preparedStatement.setDouble(1, this.saldo);
				preparedStatement.setInt(2, this.numConta);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				out.println(e);
			}

		} else {
			out.println("Voce nao possui saldo suficiente");
		}

		out.println("Seu novo saldo: " + this.saldo + "R$");
	}

	public void pagarMensal(Connection conn) {
		var taxa_cc = 12.00;
		var taxa_cp = 20.00;

		if (CC.equals(this.tipo)) {

			this.saldo -= taxa_cc;
			out.println("Foi cobrada a mensalidade de: " + taxa_cc + "R$");
		} else if (CP.equals(this.tipo)) {

			this.saldo -= taxa_cp;
			out.println("Foi cobrada a mensalidade de: " + taxa_cp + "R$");
		}

		try {
			var preparedStatement = conn.prepareStatement("update conta set saldo = ? where numConta = ?");
			preparedStatement.setDouble(1, this.saldo);
			preparedStatement.setInt(2, this.numConta);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			out.println(e);
		}

		out.println("Seu novo saldo: " + this.saldo + "R$");
		out.println("");
	}
}