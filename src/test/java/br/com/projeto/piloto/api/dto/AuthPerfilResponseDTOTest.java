package br.com.projeto.piloto.api.dto;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.projeto.piloto.adapter.in.web.dto.AuthPerfilResponseDTO;

class AuthPerfilResponseDTOTest {

    @Test
    @DisplayName("Deve validar a criação e acesso aos dados do Record")
    void deveValidarDados() {
        Long id = 10L;
        String perfil = "GERENTE";
        String app = "Sistema Financeiro";
        AuthPerfilResponseDTO dto = new AuthPerfilResponseDTO(id, perfil, app);
        assertAll("Campos do DTO",
            () -> assertEquals(id, dto.id()),
            () -> assertEquals(perfil, dto.nmPerfil()),
            () -> assertEquals(app, dto.nmAplicativo())
        );
    }

    @Test
    @DisplayName("Cobre métodos automáticos do Record (equals, hashCode e toString)")
    void deveValidarMetodosPadrao() {
        AuthPerfilResponseDTO dto1 = new AuthPerfilResponseDTO(1L, "ADMIN", "APP1");
        AuthPerfilResponseDTO dto2 = new AuthPerfilResponseDTO(1L, "ADMIN", "APP1");
        AuthPerfilResponseDTO dto3 = new AuthPerfilResponseDTO(2L, "USER", "APP2");
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
        assertTrue(dto1.toString().contains("nmPerfil=ADMIN"));
    }
}
