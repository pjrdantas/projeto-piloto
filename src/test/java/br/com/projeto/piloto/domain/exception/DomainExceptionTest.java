package br.com.projeto.piloto.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class DomainExceptionTest {

    @Test
    void deveCriarComMensagemInformada() {
        DomainException ex = new DomainException("erro de negócio");
        assertEquals("erro de negócio", ex.getMessage());
    }

    @Test
    void deveSerLancadaEPermitidaCaptura() {
        DomainException thrown = assertThrows(DomainException.class, () -> {
            throw new DomainException("lançada");
        });
        assertEquals("lançada", thrown.getMessage());
        assertTrue(thrown instanceof RuntimeException);
    }
}