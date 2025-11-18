package br.com.projeto.piloto.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SwaggerConfigTest {

    @Test
    void customOpenAPI_deveRetornarOpenAPIComInfoConfigurada() {
        SwaggerConfig config = new SwaggerConfig();

        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Projeto Piloto API", openAPI.getInfo().getTitle());
        assertEquals("API de login com Spring Boot, JWT e Oracle", openAPI.getInfo().getDescription());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
}