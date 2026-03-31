package com.hrc.almox.model.enuns;

public enum PerfilUsuario {
    ADMIN,      // Tudo liberado
    GERENTE,    // Movimentações, Histórico e Estoque (Sem Gestão de Usuários)
    OPERADOR,   // Estoque e Movimentar
    CONSULTOR,   // Apenas visualização de Estoque
    INATIVO // Usuario inativos
}
