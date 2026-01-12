package br.com.projeto.piloto.api.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.projeto.piloto.adapter.in.web.dto.AuthResponseDTO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

class AuthResponseDTOTest {

    @Test
    @DisplayName("Deve validar a criação e acesso aos dados de autenticação")
    void deveValidarAcessoDados() {
        String token = "jwt-token";
        String refresh = "refresh-token";
        String user = "admin";
        String nome = "Administrador do Sistema";
        Set<String> perfis = Set.of("ROLE_ADMIN", "ROLE_USER");
        AuthResponseDTO dto = new AuthResponseDTO(token, refresh, user, nome, perfis);
        assertAll("Verificação de campos do DTO de Resposta Auth",
            () -> assertEquals(token, dto.token()),
            () -> assertEquals(refresh, dto.refreshToken()),
            () -> assertEquals(user, dto.usuario()),
            () -> assertEquals(nome, dto.nome()),
            () -> assertEquals(perfis, dto.perfis())
        );
    }

    @Test
    @DisplayName("Deve cobrir métodos implícitos do Record (equals, hashCode e toString)")
    void deveValidarMetodosPadrao() {
        Set<String> perfis = Set.of("ROLE_USER");
        AuthResponseDTO dto1 = new AuthResponseDTO("tk", "rf", "u", "n", perfis);
        AuthResponseDTO dto2 = new AuthResponseDTO("tk", "rf", "u", "n", perfis);
        AuthResponseDTO dto3 = new AuthResponseDTO("outro", "rf", "u", "n", perfis);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotNull(dto1.toString());
        assertTrue(dto1.toString().contains("usuario=u"));
        assertTrue(dto1.toString().contains("perfis=[ROLE_USER]"));
    }
}
