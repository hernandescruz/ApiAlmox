package com.hrc.almox.controller;
import com.hrc.almox.model.Finalidade;
import com.hrc.almox.repository.FinalidadeRepository;
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

@Tag(name = "Finalidade", description = "Endpoints para gerenciamento de finalidades de movimentação")
@RestController
@RequestMapping("/finalidades")
public class FinalidadeController {

    @Autowired
    private FinalidadeRepository repository;

    @Operation(summary = "Listar todas", description = "Retorna uma lista contendo todas as finalidades cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro de comunicação com o banco de dados\"}")))
    })
    @GetMapping
    public List<Finalidade> listar() {
        return repository.findAll();
    }

    @Operation(summary = "Criar finalidade", description = "Cadastra uma nova finalidade no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Finalidade criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"A descrição é obrigatória\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar salvar.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro interno inesperado na criação do recurso\"}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Finalidade criar(@RequestBody Finalidade finalidade) {
        return repository.save(finalidade);
    }
}