package br.com.projeto.piloto.adapter.out.jpa.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.projeto.piloto.adapter.in.web.dto.AuthPerfilRequestDTO;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPermissao;
import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.model.AuthPermissaoModel;

class AuthPerfilMapperTest {

    @Test
    @DisplayName("Cobertura total: Construtor, Nulos e Branches")
    void deveCobrirTudo() {
        assertNotNull(new AuthPerfilMapper());
        AuthPerfilRequestDTO dto = new AuthPerfilRequestDTO("ADMIN", 1L, null);
        assertNotNull(AuthPerfilMapper.toDomain(dto, null, null).getPermissoes());
        assertFalse(AuthPerfilMapper.toDomain(dto, Set.of(new AuthPermissaoModel()), null).getPermissoes().isEmpty());
        assertNull(AuthPerfilMapper.toDomain((AuthPerfil) null));
        
        AuthPerfil e1 = AuthPerfil.builder().permissoes(null).build();
        assertTrue(AuthPerfilMapper.toDomain(e1).getPermissoes().isEmpty());

        AuthPerfil e2 = AuthPerfil.builder()
                .permissoes(Set.of(AuthPermissao.builder().id(1L).nmPermissao("P").build()))
                .build();
        assertFalse(AuthPerfilMapper.toDomain(e2).getPermissoes().isEmpty());
        assertNull(AuthPerfilMapper.toEntity(null));
        
        AuthPerfilModel m1 = AuthPerfilModel.builder().aplicativo(null).permissoes(null).build();
        AuthPerfil resM1 = AuthPerfilMapper.toEntity(m1);
        assertNull(resM1.getAplicativo());
        assertTrue(resM1.getPermissoes().isEmpty());

        AuthPerfilModel m2 = AuthPerfilModel.builder()
                .aplicativo(AplicativosModel.builder().id(1L).build())
                .permissoes(Set.of(AuthPermissaoModel.builder().id(1L).build()))
                .build();
        AuthPerfil resM2 = AuthPerfilMapper.toEntity(m2);
        assertNotNull(resM2.getAplicativo());
        assertFalse(resM2.getPermissoes().isEmpty());
        assertNull(AuthPerfilMapper.toResponse(null));
        
        var r1 = AuthPerfilMapper.toResponse(AuthPerfilModel.builder().aplicativo(null).build());
        assertNull(r1.nmAplicativo());

        var r2 = AuthPerfilMapper.toResponse(AuthPerfilModel.builder()
                .aplicativo(AplicativosModel.builder().nmAplicativo("App").build()).build());
        assertEquals("App", r2.nmAplicativo());
    }
}
