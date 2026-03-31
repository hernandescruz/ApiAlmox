package com.hrc.almox.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais de acesso do usuário para autenticação")
public record DadosAutenticacao(
        @Schema(description = "Login/Nome de usuário", example = "admin")
        String usuario,
        @Schema(description = "Senha do usuário", example = "123456")
        String password
) {
}
