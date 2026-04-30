package com.hrc.almox.controller;



import com.hrc.almox.model.AuditoriaLog;
import com.hrc.almox.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaRepository repository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Apenas administradores podem ver os logs
    public List<AuditoriaLog> listarTodos() {
        return repository.findAllByOrderByDataHoraDesc();
    }
}