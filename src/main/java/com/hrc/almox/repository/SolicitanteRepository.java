package com.hrc.almox.repository;



import com.hrc.almox.model.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Integer> {
    boolean existsByNome(String nome);

    Optional<Solicitante> findByNome(String nome);
}
