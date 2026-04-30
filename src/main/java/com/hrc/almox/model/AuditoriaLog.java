package com.hrc.almox.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_logs", catalog = "almoxarifado")
@Data
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dataHora;

    private String usuario; // Nome do usuário que fez a ação

    private String modulo; // Ex: "ITENS", "USUARIOS", "MOVIMENTACAO"

    private String acao; // Ex: "ALTERAÇÃO DE PREÇO", "INATIVAÇÃO", "AJUSTE MANUAL"

    @Column(columnDefinition = "TEXT")
    private String detalhes; // Descrição detalhada: "Item 101: Preço mudou de 10.0 para 12.5"
}