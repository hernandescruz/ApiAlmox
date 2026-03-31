package com.hrc.almox.model.enuns;

import lombok.Getter;

@Getter
public enum TipoMovimento {
    ENTRADA("Entrada de Material"),
    SAIDA("Saída para Consumo"),
    ENTRADA_AJUSTE("Ajuste de Inventário (Entrada)"),
    SAIDA_AJUSTE("Ajuste de Inventário (Saída)");

    private final String descricao;

    TipoMovimento(String descricao) {
        this.descricao = descricao;
    }
}