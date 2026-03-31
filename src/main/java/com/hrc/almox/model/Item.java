package com.hrc.almox.model;

import com.hrc.almox.model.enuns.UnidadeMedida;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens",catalog = "almoxarifado")
@Data // Gera Getters, Setters, toString, equals e hashCode via Lombok
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_item", unique = true, nullable = false)
    private Integer codigoItem;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false)
    private UnidadeMedida unidadeMedida;

    private String localizacao;

    @Column(name = "estoque_minimo")
    private BigDecimal estoqueMinimo = BigDecimal.ZERO;

    @Column(name = "estoque_atual")
    private BigDecimal estoqueAtual = BigDecimal.ZERO;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}