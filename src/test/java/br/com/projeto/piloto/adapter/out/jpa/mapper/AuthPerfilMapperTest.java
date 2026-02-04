package br.com.projeto.piloto.adapter.out.jpa.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPermissao;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;

class AuthPerfilMapperTest {

    @Test
    @DisplayName("Cobertura total do Mapper")
    void deveCobrirTudo() {
        assertNotNull(new AuthPerfilMapper());

         
        AuthPerfil e1 = AuthPerfil.builder().nmPerfil("TESTE").build();
        AuthPerfilModel resD1 = AuthPerfilMapper.toDomain(e1);
        assertNotNull(resD1);

        
        AuthPerfilModel m1 = AuthPerfilModel.builder().nmPerfil("M1").build();
        AuthPerfil resM1 = AuthPerfilMapper.toEntity(m1);
        assertNotNull(resM1);
        assertEquals("M1", resM1.getNmPerfil());

         
        AuthPerfil e2 = AuthPerfil.builder()
                .permissoes(Set.of(AuthPermissao.builder().id(1L).nmPermissao("P").build()))
                .build();
        assertFalse(AuthPerfilMapper.toDomain(e2).getPermissoes().isEmpty());
        
         
        assertNull(AuthPerfilMapper.toResponse(null));
        AuthPerfilModel m3 = AuthPerfilModel.builder().nmPerfil("ADMIN").build();
        assertNotNull(AuthPerfilMapper.toResponse(m3));
    }
}
