package com.hrc.almox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 1. Em vez de "*", coloque a URL exata do seu frontend
        config.setAllowedOrigins(List.of("https://sge.hrcsistemas.com.br"));

        // 2. Garanta que todos os métodos necessários estão aqui
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 3. Permita todos os headers e a exposição da Authorization
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));

        // 4. Se estiver usando HTTPS e domínios reais, mude para true se precisar de cookies,
        // ou mantenha false se for apenas JWT puro (como o nosso caso)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;


    }
}
