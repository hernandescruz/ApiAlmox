package com.hrc.almox.controller;

import com.hrc.almox.model.CentroCusto;
import com.hrc.almox.repository.CentroCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Centro de Custo", description = "Endpoints para gerenciamento de centros de custo do almoxarifado")
@RestController
@RequestMapping("/centros-custo")
public class CentroCustoController {

    @Autowired
    private CentroCustoRepository repository;

    @Operation(summary = "Listar todos", description = "Retorna uma lista contendo todos os centros de custo cadastrados na aplicação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro de comunicação com o banco de dados\"}")))
    })
    @GetMapping
    public List<CentroCusto> listar() {
        return repository.findAll();
    }

    @Operation(summary = "Criar centro de custo", description = "Cadastra um novo centro de custo no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Centro de custo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"O campo nome é obrigatório\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar salvar.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro interno inesperado na criação do recurso\"}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CentroCusto criar(@RequestBody CentroCusto centroCusto) {
        return repository.save(centroCusto);
    }
}