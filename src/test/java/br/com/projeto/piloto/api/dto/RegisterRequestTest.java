package br.com.projeto.piloto.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RegisterRequestTest {

    @Test
    void deveCriarEExporCamposCorretamente() {
        RegisterRequest req = new RegisterRequest("João Silva", "joao", "senha123");

        assertEquals("João Silva", req.nome());
        assertEquals("joao", req.login());
        assertEquals("senha123", req.senha());
    }

    @Test
    void equalsEHashCodeFuncionamCorretamente() {
        RegisterRequest a = new RegisterRequest("Ana", "ana", "s1");
        RegisterRequest b = new RegisterRequest("Ana", "ana", "s1");
        RegisterRequest c = new RegisterRequest("Ana2", "ana", "s1");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContemInformacoesEsperadas() {
        RegisterRequest req = new RegisterRequest("Carlos", "carlos", "minhaSenha");
        String s = req.toString();

        assertTrue(s.contains("Carlos"));
        assertTrue(s.contains("carlos"));
        assertTrue(s.contains("minhaSenha"));
    }
}