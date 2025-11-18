package br.com.projeto.piloto.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class AuthResponseTest {

    @Test
    void deveCriarEExporCamposCorretamente() {
        AuthResponse resp = new AuthResponse("token123", "refresh456", "usuario1");

        assertEquals("token123", resp.token());
        assertEquals("refresh456", resp.refreshToken());
        assertEquals("usuario1", resp.usuario());
    }

    @Test
    void equalsEHashCodeFuncionam() {
        AuthResponse a = new AuthResponse("t", "r", "u");
        AuthResponse b = new AuthResponse("t", "r", "u");
        AuthResponse c = new AuthResponse("t2", "r", "u");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContemInformacoesEsperadas() {
        AuthResponse resp = new AuthResponse("tok", "ref", "usr");
        String s = resp.toString();

        assertTrue(s.contains("tok"));
        assertTrue(s.contains("ref"));
        assertTrue(s.contains("usr"));
    }
}
