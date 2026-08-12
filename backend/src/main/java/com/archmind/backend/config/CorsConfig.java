package com.archmind.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    /**
     * A single CorsConfigurationSource bean is all that is needed.
     *
     * Spring Security's .cors(cors -> cors.configurationSource(...)) picks this
     * up directly, so there is no need for a separate CorsFilter bean.
     * Having both a CorsFilter bean AND a CorsConfigurationSource bean can
     * cause duplicate CORS headers which some browsers reject.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Allowed origins (Vite dev server ports)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        ));

        // Allow all standard HTTP methods including OPTIONS (preflight)
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Allow all request headers
        config.setAllowedHeaders(List.of("*"));

        // Expose headers the browser JS can read
        config.setExposedHeaders(List.of("Authorization"));

        // Allow cookies / Authorization header to be sent
        config.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}