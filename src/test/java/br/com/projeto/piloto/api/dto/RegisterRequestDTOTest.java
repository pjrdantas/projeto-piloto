package br.com.projeto.piloto.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RegisterRequestDTOTest {

    @Test
    void deveCriarEExporCamposCorretamente() {
        RegisterRequestDTO req = new RegisterRequestDTO("João Silva", "joao", "senha123");

        assertEquals("João Silva", req.nome());
        assertEquals("joao", req.login());
        assertEquals("senha123", req.senha());
    }

    @Test
    void equalsEHashCodeFuncionamCorretamente() {
        RegisterRequestDTO a = new RegisterRequestDTO("Ana", "ana", "s1");
        RegisterRequestDTO b = new RegisterRequestDTO("Ana", "ana", "s1");
        RegisterRequestDTO c = new RegisterRequestDTO("Ana2", "ana", "s1");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void toStringContemInformacoesEsperadas() {
        RegisterRequestDTO req = new RegisterRequestDTO("Carlos", "carlos", "minhaSenha");
        String s = req.toString();

        assertTrue(s.contains("Carlos"));
        assertTrue(s.contains("carlos"));
        assertTrue(s.contains("minhaSenha"));
    }
}