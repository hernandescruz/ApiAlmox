package com.hrc.almox.controller;

import com.hrc.almox.model.Usuario;
import com.hrc.almox.model.enuns.PerfilUsuario;
import com.hrc.almox.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetamos o BCrypt aqui

    @GetMapping
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criar(@RequestBody Usuario usuario) {
        // REGRA DE SEGURANÇA CRÍTICA: Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);

        return repository.save(usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Integer id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setPerfil(PerfilUsuario.INATIVO); // Muda o perfil para inativo
        repository.save(usuario);
    }

    // 2. Alteração de Senha
    @PatchMapping("/{id}/alterar-senha")
    @Transactional
    public ResponseEntity<?> alterarSenha(@PathVariable Integer id, @RequestBody DadosSenha dados) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dados.password() == null || dados.password().isBlank()) {
            return ResponseEntity.badRequest().body("A senha não pode estar vazia");
        }

        // Criptografa a nova senha antes de salvar
        usuario.setPassword(passwordEncoder.encode(dados.password()));
        repository.save(usuario);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }


    record DadosSenha(String password) {}
}
