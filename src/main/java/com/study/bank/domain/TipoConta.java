package com.study.bank.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoConta 
{
    CP("cp", "Conta Poupanca", 1),
    CC("cc", "Conta Corrente", 2);

    private final String sigla;
    private final String descricao;
    private final int code;

    public static Optional<TipoConta> getContaTipoPorSigla(String value) {
        return Arrays.stream(TipoConta.values())
            .filter(accStatus -> accStatus.sigla.equals(value) 
                || accStatus.descricao.equals(value))
            .findFirst();
    }
    
    public static Optional<TipoConta> getContaTipoPorCodigo(int value) {
        return Arrays.stream(TipoConta.values())
            .filter(accStatus -> accStatus.code == value)
            .findFirst();
    }
}