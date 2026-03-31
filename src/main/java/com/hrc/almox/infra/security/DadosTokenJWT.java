package com.hrc.almox.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de retorno contendo o token JWT gerado após o login")
public record DadosTokenJWT(
        @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token, 
        @Schema(description = "Dados adicionais do usuário (ex: roles)", example = "[\"ROLE_ADMIN\"]")
        String[] dados
) {
}

