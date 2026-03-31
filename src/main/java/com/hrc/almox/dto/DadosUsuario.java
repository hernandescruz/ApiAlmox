package com.hrc.almox.dto;

import com.hrc.almox.model.Usuario;

public record DadosUsuario(

        int id,
        String usuario




) {
    public DadosUsuario(Usuario u){
        this(
                u.getId(),

                u.getNome()

        );
    }
}