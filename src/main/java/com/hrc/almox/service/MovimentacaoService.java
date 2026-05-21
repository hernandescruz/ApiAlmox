package com.hrc.almox.service;

import com.hrc.almox.dto.MovimentacaoRequestDTO;
import com.hrc.almox.model.*;
import com.hrc.almox.model.enuns.TipoMovimento;
import com.hrc.almox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor // Gera o construtor para injeção de dependência (substitui o @Autowired)
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemRepository itemRepository;
    private final CentroCustoRepository ccRepository;
    private final FinalidadeRepository finalidadeRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Movimentacao registrarMovimentacao(MovimentacaoRequestDTO dto) {
        // 1. Aplicar regras de preenchimento automático para casos especiais
        ajustarDadosDeSistema(dto);

        // 2. Buscar Entidades Reais (Garante que os dados existem e estão atualizados)
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3. Validar e Calcular Saldo
        atualizarEstoqueItem(item, dto.getTipoMovimento(), dto.getQuantidade());

        // 4. Mapear DTO para Entidade
        Movimentacao mov = new Movimentacao();
        mov.setTipoMovimento(dto.getTipoMovimento());
        mov.setQuantidade(dto.getQuantidade());
        mov.setItem(item);
        mov.setUsuario(usuario);
        mov.setValorTotalMovimentacao(item.getPrecoUnitario().multiply(dto.getQuantidade()));

        // Para os campos abaixo, buscamos a referência (proxy) para performance
        mov.setCentroCusto(ccRepository.getReferenceById(dto.getCentroCustoId()));
        mov.setFinalidade(finalidadeRepository.getReferenceById(dto.getFinalidadeId()));
        mov.setSolicitante(solicitanteRepository.getReferenceById(dto.getSolicitanteId()));

        // 5. Salvar e Retornar
        itemRepository.save(item);
        return movimentacaoRepository.save(mov);
    }

    private void atualizarEstoqueItem(Item item, TipoMovimento tipo, BigDecimal qtd) {
        BigDecimal estoqueAtual = item.getEstoqueAtual();


        if (tipo.isEntrada()) {
            item.setEstoqueAtual(estoqueAtual.add(qtd));
        } else {
            // Regra: Não permite estoque negativo (isSaida)
            if (estoqueAtual.compareTo(qtd) < 0) {
                throw new RuntimeException("Saldo insuficiente! Atual: " + estoqueAtual + ", Solicitado: " + qtd);
            }
            item.setEstoqueAtual(estoqueAtual.subtract(qtd));
        }
    }

    /**
     * Aplica regras automáticas para Ajustes e Entradas,
     * garantindo que os IDs no DTO apontem para os registros de sistema.
     */
    private void ajustarDadosDeSistema(MovimentacaoRequestDTO dto) {
        TipoMovimento tipo = dto.getTipoMovimento();

        // USANDO SEU NOVO MÉTODO .isAjuste()
        if (tipo.isAjuste()) {
            dto.setCentroCustoId(getOrCreateCC("INVENTÁRIO").getId());
            dto.setFinalidadeId(getOrCreateFinalidade("INVENTÁRIO").getId());
            dto.setSolicitanteId(getOrCreateSolicitante("ADM").getId());
        }
        // ENTRADA NORMAL (COMPRA/REPOSIÇÃO)
        else if (tipo == TipoMovimento.ENTRADA) {
            dto.setCentroCustoId(getOrCreateCC("ALMOXARIFADO").getId());
            dto.setFinalidadeId(getOrCreateFinalidade("REPOSIÇÃO").getId());
            dto.setSolicitanteId(getOrCreateSolicitante("ADM").getId());
        }
    }

    // Métodos Auxiliares de "Busca ou Criação"
    private CentroCusto getOrCreateCC(String nome) {
        return ccRepository.findByNome(nome).orElseGet(() -> {
            CentroCusto nc = new CentroCusto();
            nc.setNome(nome);
            nc.setAtivo(true);
            return ccRepository.save(nc);
        });
    }

    private Finalidade getOrCreateFinalidade(String nome) {
        return finalidadeRepository.findByNome(nome).orElseGet(() -> {
            Finalidade nf = new Finalidade();
            nf.setNome(nome);
            nf.setAtivo(true);
            return finalidadeRepository.save(nf);
        });
    }

    private Solicitante getOrCreateSolicitante(String nome) {
        return solicitanteRepository.findByNome(nome).orElseGet(() -> {
            Solicitante ns = new Solicitante();
            ns.setNome(nome);
            ns.setAtivo(true);
            return solicitanteRepository.save(ns);
        });
    }

    public List<Movimentacao> listarTodas(Pageable pageable) {
        return movimentacaoRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}