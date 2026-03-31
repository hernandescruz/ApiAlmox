package com.hrc.almox.dto;

import com.hrc.almox.model.enuns.UnidadeMedida;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de um novo item no sistema")
@Data
public class ItemRequestDTO {

    @Schema(description = "Código numérico identificador do item (geralmente lido via código de barras)", example = "1050")
    @NotNull(message = "O código do item é obrigatório")
    private Integer codigoItem;

    @Schema(description = "Descrição detalhada do item para facilitar a busca", example = "Caneta esferográfica azul")
    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricao;

    @Schema(description = "Unidade de medida padrão adotada para o controle do item (ex: UNIDADE, CAIXA, PACOTE)", example = "UNIDADE")
    @NotNull(message = "A unidade de medida é obrigatória")
    private UnidadeMedida unidadeMedida;

    @Schema(description = "Localização física do item dentro do estabelecimento/almoxarifado", example = "Corredor A, Prateleira 3")
    private String localizacao;

    @Schema(description = "Quantidade mínima orientativa que deverá permanecer em estoque", example = "10.0")
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo")
    private BigDecimal estoqueMinimo;

    @Schema(description = "Valor base de custo unitário estimado do item", example = "2.50")
    @Min(value = 0, message = "O preço unitário não pode ser negativo")
    private BigDecimal precoUnitario;
}
