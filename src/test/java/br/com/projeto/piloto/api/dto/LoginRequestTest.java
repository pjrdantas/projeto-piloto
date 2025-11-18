package br.com.projeto.piloto.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class LoginRequestTest {

    @Test
    void deveCriarEExporCamposCorretamente() {
        LoginRequest req = new LoginRequest("usuario1", "senha123");

        assertEquals("usuario1", req.login());
        assertEquals("senha123", req.senha());
    }

    @Test
    void equalsEHashCodeFuncionamCorretamente() {
        LoginRequest a = new LoginRequest("u", "s");
        LoginRequest b = new LoginRequest("u", "s");
        LoginRequest c = new LoginRequest("u2", "s");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContemInformacoesEsperadas() {
        LoginRequest req = new LoginRequest("meuUser", "minhaSenha");
        String s = req.toString();

        assertTrue(s.contains("meuUser"));
        assertTrue(s.contains("minhaSenha"));
    }
}