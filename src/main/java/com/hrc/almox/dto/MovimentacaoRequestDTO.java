package com.hrc.almox.dto;

import com.hrc.almox.model.enuns.TipoMovimento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registro de uma nova movimentação de item no estoque")
@Data
public class MovimentacaoRequestDTO {

    @Schema(description = "Tipo da movimentação: ENTRADA ou SAIDA", example = "ENTRADA")
    @NotNull(message = "O tipo de movimento é obrigatório")
    private TipoMovimento tipoMovimento;

    @Schema(description = "ID do item movimentado", example = "1")
    @NotNull(message = "O ID do item é obrigatório")
    private Long itemId;

    @Schema(description = "Quantidade a ser movimentada", example = "50.0")
    @NotNull(message = "A quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @Schema(description = "ID do usuário responsável pelo registro da movimentação", example = "2")
    @NotNull(message = "O ID do usuário é obrigatório")
    private Integer usuarioId;

    @Schema(description = "ID do centro de custo relacionado à movimentação (especialmente para saídas)", example = "3")
    @NotNull(message = "O ID do centro de custo é obrigatório")
    private Integer centroCustoId;

    @Schema(description = "ID da finalidade da movimentação", example = "4")
    @NotNull(message = "O ID da finalidade é obrigatória")
    private Integer finalidadeId;
}
