package br.com.projeto.piloto.adapter.out.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class RoleEntityTest {

    @Test
    void builderDeveCriarInstanciaComCamposInformados() {
        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .nome("ADMIN")
                .build();

        assertEquals(1L, roleEntity.getId());
        assertEquals("ADMIN", roleEntity.getNome());
    }

    @Test
    void usuariosDeveVirInicializadoComoSetVazioPeloBuilder() {
        RoleEntity roleEntity = RoleEntity.builder().build();

        assertNotNull(roleEntity.getUsuarios());
        assertTrue(roleEntity.getUsuarios().isEmpty());
    }

    @Test
    void deveAdicionarERemoverUsuarioDoSetUsuarios() {
        RoleEntity roleEntity = RoleEntity.builder().build();

        UserEntity userEntity = Mockito.mock(UserEntity.class);

        roleEntity.getUsuarios().add(userEntity);
        assertTrue(roleEntity.getUsuarios().contains(userEntity));
        assertEquals(1, roleEntity.getUsuarios().size());

        roleEntity.getUsuarios().remove(userEntity);
        assertFalse(roleEntity.getUsuarios().contains(userEntity));
        assertTrue(roleEntity.getUsuarios().isEmpty());
    }
}