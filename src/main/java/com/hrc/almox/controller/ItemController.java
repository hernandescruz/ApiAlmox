package com.hrc.almox.controller;

import com.hrc.almox.dto.ItemRequestDTO;
import com.hrc.almox.model.Item;
import com.hrc.almox.repository.ItemRepository;
import com.hrc.almox.dominio.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Itens", description = "Endpoints para gerenciamento do cadastro de itens do almoxarifado")
@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Operation(summary = "Listar todos os itens", description = "Retorna uma lista com todos os itens cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro de comunicação com o banco de dados\"}")))
    })
    @GetMapping
    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    @Operation(summary = "Criar novo item", description = "Cadastra um novo item no estoque com a quantidade inicial zerada, se não informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"O código do item é obrigatório\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar salvar.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro ao processar criação de item\"}")))
    })
    @PostMapping
    public ResponseEntity<Item> criar(@RequestBody @Valid ItemRequestDTO dto) {
        Item item = new Item();
        // Copia as propriedades do DTO para a Entidade de forma simples
        BeanUtils.copyProperties(dto, item);

        // Garantimos que o estoque inicial seja zero se não for informado
        if (item.getEstoqueAtual() == null) {
            item.setEstoqueAtual(java.math.BigDecimal.ZERO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(item));
    }

    // Listagem geral com filtros opcionais
    @Operation(summary = "Buscar itens com filtros", description = "Realiza busca de itens por descrição, localização ou alertas de estoque mínimo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Parâmetro inválido\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro ao acessar dados de itens\"}")))
    })
    @GetMapping("/busca")
    public List<Item> buscar(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) Boolean abaixoMinimo) {

        // 1. Alerta de Estoque Baixo
        if (Boolean.TRUE.equals(abaixoMinimo)) {
            return itemRepository.findItensAbaixoDoMinimo();
        } else

        // 2. Busca por Descrição
        if (descricao != null && !descricao.isEmpty()) {
            return itemRepository.findByDescricaoContainingIgnoreCase(descricao);
        } else

        // 3. Busca por Localização (Corrigido aqui)
        if (localizacao != null && !localizacao.isEmpty()) {
            return itemRepository.findByLocalizacaoContainingIgnoreCase(localizacao);
        }else {
            throw new ValidacaoException("Parametro inválido");
        }

        // Se nenhum parâmetro for enviado, retorna lista vazia ou erro 400
        // para evitar carregar o banco inteiro sem querer no PWA
       // return java.util.Collections.emptyList();
    }

    // Busca rápida por código exato (Útil para o leitor de código de barras)
    @Operation(summary = "Buscar item por código", description = "Busca detalhada de um item pelo seu código exato (ex: uso de leitor de código de barras).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Item com código 123 não encontrado.\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"erro\": \"Erro ao consultar o banco de dados\"}")))
    })
    @GetMapping("/codigo/{codigo}")
    public Item buscarPorCodigo(@PathVariable Integer codigo) {
        return itemRepository.findByCodigoItem(codigo)
                .orElseThrow(() -> new RuntimeException("Item com código " + codigo + " não encontrado."));
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));
        item.setAtivo(false);
        itemRepository.save(item);
    }

    @PostMapping("/lote")
    @Transactional
    public ResponseEntity<?> criarEmLote(@RequestBody List<ItemRequestDTO> dtos) {
        List<Item> novosItens = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        for (ItemRequestDTO dto : dtos) {
            // Verifica se o código já existe para não quebrar o banco
            if (itemRepository.findByCodigoItem(dto.getCodigoItem()).isPresent()) {
                erros.add("Código " + dto.getCodigoItem() + " já cadastrado (Item: " + dto.getDescricao() + ")");
                continue;
            }

            Item item = new Item();
            BeanUtils.copyProperties(dto, item);

            if (item.getEstoqueAtual() == null) item.setEstoqueAtual(BigDecimal.ZERO);
            if (item.getEstoqueMinimo() == null) item.setEstoqueMinimo(BigDecimal.ZERO);

            novosItens.add(item);
        }

        if (!novosItens.isEmpty()) {
            itemRepository.saveAll(novosItens);
        }

        if (!erros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(erros);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Importação concluída com sucesso!");
    }
}