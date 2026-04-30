package com.hrc.almox.service;

import com.hrc.almox.model.AuditoriaLog;
import com.hrc.almox.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {
    @Autowired
    private AuditoriaRepository repository;

    public void registrar(String usuario, String modulo, String acao, String detalhes) {
        AuditoriaLog log = new AuditoriaLog();
        log.setUsuario(usuario);
        log.setModulo(modulo);
        log.setAcao(acao);
        log.setDetalhes(detalhes);
        repository.save(log);
    }
}