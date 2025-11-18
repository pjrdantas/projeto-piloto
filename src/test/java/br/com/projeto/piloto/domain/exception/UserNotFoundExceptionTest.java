package br.com.projeto.piloto.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class UserNotFoundExceptionTest {

    @Test
    void deveCriarComMensagemInformada() {
        UserNotFoundException ex = new UserNotFoundException("Usuário não encontrado");
        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveSerSubclasseDeDomainExceptionEPermitirCaptura() {
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            throw new UserNotFoundException("não existe");
        });

        assertEquals("não existe", thrown.getMessage());
        assertTrue(thrown instanceof DomainException);
    }
}
