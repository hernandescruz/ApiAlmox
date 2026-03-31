package com.hrc.almox.service;

import com.hrc.almox.model.Movimentacao;
import com.hrc.almox.model.enuns.TipoMovimento;
import com.hrc.almox.repository.ItemRepository;
import com.hrc.almox.model.Item;
import com.hrc.almox.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Movimentacao registrarMovimentacao(Movimentacao movimentacao) {
        // 1. Buscar o item atualizado no banco
        System.out.println(movimentacao);
        Item item = itemRepository.findById(movimentacao.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        BigDecimal quantidade = movimentacao.getQuantidade();
        BigDecimal estoqueAtual = item.getEstoqueAtual();

        // 2. Lógica de cálculo de saldo baseada no TipoMovimento
        if (movimentacao.getTipoMovimento() == TipoMovimento.ENTRADA ||
                movimentacao.getTipoMovimento() == TipoMovimento.ENTRADA_AJUSTE) {

            item.setEstoqueAtual(estoqueAtual.add(quantidade));

        } else if (movimentacao.getTipoMovimento() == TipoMovimento.SAIDA ||
                movimentacao.getTipoMovimento() == TipoMovimento.SAIDA_AJUSTE) {

            // RN01: Impedir Saldo Negativo (exceto se você decidir permitir em ajustes)
            if (estoqueAtual.compareTo(quantidade) < 0) {
                throw new RuntimeException("Saldo insuficiente para realizar esta saída! Estoque atual: " + estoqueAtual);
            }

            item.setEstoqueAtual(estoqueAtual.subtract(quantidade));
        }

        // 3. Calcular valor total da movimentação (Preço Unitário x Quantidade)
        movimentacao.setValorTotalMovimentacao(item.getPrecoUnitario().multiply(quantidade));

        // 4. Salvar item atualizado e a movimentação
        itemRepository.save(item);
        System.out.println("antes do save");
        System.out.println(movimentacao);
        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> listarTodas(){
        return movimentacaoRepository.findAllByOrderByCreatedAtDesc();
    }

}
