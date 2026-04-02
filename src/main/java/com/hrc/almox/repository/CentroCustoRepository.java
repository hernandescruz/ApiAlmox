package com.hrc.almox.repository;

import com.hrc.almox.model.CentroCusto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {
    boolean existsByNome(String nome);

    Optional<CentroCusto> findByNome(String nome);

}
