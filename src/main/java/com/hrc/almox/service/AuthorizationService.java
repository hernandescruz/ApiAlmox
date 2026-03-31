package com.hrc.almox.service;

import com.hrc.almox.model.Usuario;
import com.hrc.almox.model.enuns.PerfilUsuario;
import com.hrc.almox.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = (Usuario) repository.findByUsuario(username);

        if (usuario.getPerfilUsuario() == PerfilUsuario.INATIVO) {
            throw new UsernameNotFoundException("Usuário inativo. Procure o administrador.");
        }

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return usuario;
    }
}