package com.study.bank.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operacao 
{
    SAQUE("saque", 1),
    DEPOSITO("deposito",2),
    PAGAMENTO("pgto", 3);

    private final String sigla;
    private final int code;

    public static Optional<Operacao> getOperacaoPorSigla(String value) {
        return Arrays.stream(Operacao.values())
            .filter(accStatus -> accStatus.sigla.equalsIgnoreCase(value))
            .findFirst();
    }
    
    public static Optional<Operacao> getOperacaoPorCodigo(int value) {
        return Arrays.stream(Operacao.values())
            .filter(accStatus -> accStatus.code == value)
            .findFirst();
    }
}