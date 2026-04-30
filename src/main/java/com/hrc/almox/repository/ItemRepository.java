package com.hrc.almox.repository;

import com.hrc.almox.model.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCodigoItem(Integer codigoItem);

    // Busca por parte da descrição (ignora maiúsculas/minúsculas)
    List<Item> findByDescricaoContainingIgnoreCase(String descricao);

    // Busca itens com estoque abaixo do mínimo (Para o Dashboard)
    @Query("SELECT i FROM Item i WHERE i.estoqueAtual < i.estoqueMinimo")
    List<Item> findItensAbaixoDoMinimo();

    List<Item> findByLocalizacaoContainingIgnoreCase(String localizacao);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id = :id")
    Optional<Item> findByIdForUpdate(@Param("id") Long id);

    @Query("SELECT i FROM Item i WHERE " +
            "(:desc is null OR UPPER(i.descricao) LIKE UPPER(CONCAT('%', :desc, '%'))) AND " +
            "(:ativo is null OR i.ativo = :ativo) AND " +
            "(:status is null OR " +
            "  (:status = 'REPOR' AND i.estoqueAtual < i.estoqueMinimo) OR " +
            "  (:status = 'OK' AND i.estoqueAtual >= i.estoqueMinimo))")
    Page<Item> findComFiltros(String desc, Boolean ativo, String status, Pageable pageable);


}
