package br.com.projeto.piloto.adapter.out.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class UserEntityTest {

    @Test
    void builderDeveCriarInstanciaComCamposInformados() {
        UserEntity userEntity = UserEntity.builder()
                .id(5L)
                .nome("João")
                .login("joao")
                .senha("secret")
                .ativo("S")
                .build();

        assertEquals(5L, userEntity.getId());
        assertEquals("João", userEntity.getNome());
        assertEquals("joao", userEntity.getLogin());
        assertEquals("secret", userEntity.getSenha());
        assertEquals("S", userEntity.getAtivo());
    }

    @Test
    void perfisDeveVirInicializadoComoSetVazioPeloBuilder() {
        UserEntity userEntity = UserEntity.builder().build();

        assertNotNull(userEntity.getPerfis());
        assertTrue(userEntity.getPerfis().isEmpty());
    }

    @Test
    void deveAdicionarERemoverPerfisDoUsuario() {
        UserEntity userEntity = UserEntity.builder().build();

        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .nome("ROLE_USER")
                .build();

        userEntity.getPerfis().add(roleEntity);
        assertTrue(userEntity.getPerfis().contains(roleEntity));
        assertEquals(1, userEntity.getPerfis().size());

        userEntity.getPerfis().remove(roleEntity);
        assertFalse(userEntity.getPerfis().contains(roleEntity));
        assertTrue(userEntity.getPerfis().isEmpty());
    }

    @Test
    void podeUsarMockedRoleNoSetPerfis() {
        UserEntity userEntity = UserEntity.builder().build();
        RoleEntity mocked = Mockito.mock(RoleEntity.class);

        userEntity.getPerfis().add(mocked);
        assertTrue(userEntity.getPerfis().contains(mocked));
    }
}