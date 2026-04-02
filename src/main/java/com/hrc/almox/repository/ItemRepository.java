package com.hrc.almox.repository;

import com.hrc.almox.model.Item;
import jakarta.persistence.LockModeType;
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


}
