package com.hrc.almox.model.enuns;

import lombok.Getter;

@Getter
public enum TipoMovimento {
    ENTRADA("Entrada de Material"),
    SAIDA("Saída para Consumo"),
    ENTRADA_AJUSTE("Ajuste de Inventário (Entrada)"),
    SAIDA_AJUSTE("Ajuste de Inventário (Saída)");

    public boolean isEntrada() {
        return this == ENTRADA || this == ENTRADA_AJUSTE;
    }

    public boolean isSaida() {
        return this == SAIDA || this == SAIDA_AJUSTE;
    }

    public boolean isAjuste() {
        return this == ENTRADA_AJUSTE || this == SAIDA_AJUSTE;
    }

    private final String descricao;

    TipoMovimento(String descricao) {
        this.descricao = descricao;
    }

}