package com.hrc.almox.controller;

import com.hrc.almox.dto.MovimentacaoRequestDTO;
import com.hrc.almox.model.*;
import com.hrc.almox.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Movimentação", description = "Endpoints para registro de movimentações (entrada e saída) de itens no estoque")
@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;



    @Operation(summary = "Registrar movimentação", description = "Criação de novo registro de entrada ou saída, atualizando a quantidade do item em estoque de acordo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimentação registrada com sucesso e estoque atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição ou regra de negócio inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Estoque insuficiente para quantidade de saída solicitada\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar salvar.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro inesperado na gravação da movimentação\"}")))
    })
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid MovimentacaoRequestDTO dto) {
        try {

          //  Movimentacao movimentacao = mapDtoToEntity(dto);

            Movimentacao novaMovimentacao = movimentacaoService.registrarMovimentacao(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Movimentacao> listarTodas(Pageable pageable) {

        return movimentacaoService.listarTodas(pageable);
    }


}
