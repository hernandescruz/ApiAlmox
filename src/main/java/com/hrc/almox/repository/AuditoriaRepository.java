package com.hrc.almox.repository;

import com.hrc.almox.model.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<AuditoriaLog, Long> {
    List<AuditoriaLog> findAllByOrderByDataHoraDesc();
}
