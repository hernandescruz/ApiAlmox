package com.hrc.almox.controller;


import com.hrc.almox.model.Solicitante;
import com.hrc.almox.repository.SolicitanteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Solicitantes", description = "Endpoints para gerenciamento de solicitantes de materiais")
@RestController
@RequestMapping("/solicitantes")
public class SolicitanteController {

    @Autowired
    private SolicitanteRepository repository;

    @Operation(summary = "Listar todas", description = "Retorna uma lista contendo todas os solicitantes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro de comunicação com o banco de dados\"}")))
    })
    @GetMapping
    public List<Solicitante> listar() {
        return repository.findAll();
    }

    @Operation(summary = "Criar solicitante", description = "Cadastra um novo solicitante no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"O nome é obrigatório\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar salvar.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro interno inesperado na criação do recurso\"}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Solicitante criar(@RequestBody Solicitante solicitante) {
        if (repository.existsByNome(solicitante.getNome())) {
            throw new RuntimeException("Solicitante já existente");
        }
        return repository.save(solicitante);
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
       Solicitante solicitante = repository.findById(id).orElseThrow(() -> new RuntimeException("Solicitante não encontrada"));
        solicitante.setAtivo(false);
       repository.save(solicitante);
    }
}