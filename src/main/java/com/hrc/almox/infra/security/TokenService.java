package com.hrc.almox.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hrc.almox.dto.DadosUsuario;
import com.hrc.almox.model.Usuario;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;



@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Getter
    private DadosUsuario dadosUsuario;



    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            String[] dados = {usuario.getId()+"",usuario.getNome(), usuario.getCargo()};


            dadosUsuario = new DadosUsuario(usuario);

            return JWT.create()
                    .withIssuer("API")
                    .withSubject(usuario.getUsuario())
                    .withExpiresAt(dataExpiracao())
                    .withClaim("Nome",usuario.getNome())
                    .withClaim("Id",usuario.getId())
                    .withArrayClaim("Dados", dados)
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }


    public String[] getDados(String token) {

        return JWT.decode(token).getClaim("Dados").asArray(String.class);

    }


    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {

            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }


    private Instant dataExpiracao() {

        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
        //return LocalDateTime.now().plusYears(10).toInstant(ZoneOffset.of("-03:00"));
    }


}
