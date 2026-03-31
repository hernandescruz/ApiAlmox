package com.hrc.almox.controller;

import com.hrc.almox.dto.DadosAutenticacao;
import com.hrc.almox.infra.security.DadosTokenJWT;
import com.hrc.almox.infra.security.TokenService;
import com.hrc.almox.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints para gerenciamento de autenticação e login")
@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;



    @Operation(summary = "Efetuar login", description = "Autentica um usuário fornecendo suas credenciais e retorna um token JWT de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Token JWT retornado."),
            @ApiResponse(responseCode = "400", description = "Dados de autenticação inválidos.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Usuário ou senha inválidos\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro interno inesperado. Tente novamente mais tarde.\"}")))
    })
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {


        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.usuario(), dados.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());


        var dados1 = tokenService.getDados(tokenJWT);




        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, dados1));
    }


}
