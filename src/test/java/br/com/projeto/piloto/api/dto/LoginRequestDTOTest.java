package br.com.projeto.piloto.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class LoginRequestDTOTest {

    @Test
    void deveCriarEExporCamposCorretamente() {
        LoginRequestDTO req = new LoginRequestDTO("usuario1", "senha123");

        assertEquals("usuario1", req.login());
        assertEquals("senha123", req.senha());
    }

    @Test
    void equalsEHashCodeFuncionamCorretamente() {
        LoginRequestDTO a = new LoginRequestDTO("u", "s");
        LoginRequestDTO b = new LoginRequestDTO("u", "s");
        LoginRequestDTO c = new LoginRequestDTO("u2", "s");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContemInformacoesEsperadas() {
        LoginRequestDTO req = new LoginRequestDTO("meuUser", "minhaSenha");
        String s = req.toString();

        assertTrue(s.contains("meuUser"));
        assertTrue(s.contains("minhaSenha"));
    }
}