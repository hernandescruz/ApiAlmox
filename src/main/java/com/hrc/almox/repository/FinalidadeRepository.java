package com.hrc.almox.repository;


import com.hrc.almox.model.Finalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinalidadeRepository extends JpaRepository<Finalidade, Integer> {
    boolean existsByNome(String nome);

    Optional<Finalidade> findByNome(String nome);
}
