package br.com.projeto.piloto.adapter.in.web.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Map;


class ErrorResponseTest {

    @Test
    void builderShouldCreateInstanceWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> validation = Map.of("campo", "não pode ser nulo");

        ErrorResponse resp = ErrorResponse.builder()
                .timestamp(now)
                .status(422)
                .error("Erro de validação")
                .message("Dados inválidos")
                .path("/api/test")
                .validationErrors(validation)
                .build();

        assertEquals(now, resp.getTimestamp());
        assertEquals(422, resp.getStatus());
        assertEquals("Erro de validação", resp.getError());
        assertEquals("Dados inválidos", resp.getMessage());
        assertEquals("/api/test", resp.getPath());
        assertEquals(validation, resp.getValidationErrors());
    }

    @Test
    void validationErrorsCanBeNull() {
        ErrorResponse resp = ErrorResponse.builder()
                .timestamp(LocalDateTime.of(2025, 11, 15, 10, 0))
                .status(500)
                .error("Erro interno")
                .message("Ocorreu um erro")
                .path("/api/internal")
                .build();

        assertNull(resp.getValidationErrors());
    }
}
