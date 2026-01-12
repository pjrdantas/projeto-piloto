package br.com.projeto.piloto.adapter.out.jpa.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.projeto.piloto.adapter.in.web.dto.AuthPerfilRequestDTO;
import br.com.projeto.piloto.adapter.in.web.dto.AuthPerfilResponseDTO;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;
import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.model.AuthPermissaoModel;

public class AuthPerfilMapper {

    public static AuthPerfilModel toDomain(AuthPerfilRequestDTO dto, Set<AuthPermissaoModel> permissoes, AplicativosModel aplicativo) {
        return AuthPerfilModel.builder()
                .nmPerfil(dto.nmPerfil())
                .aplicativo(aplicativo)
                .permissoes(permissoes != null ? permissoes : Collections.emptySet())
                .build();
    }

    public static AuthPerfilModel toDomain(AuthPerfil entity) {
        if (entity == null) return null;
        return AuthPerfilModel.builder()
                .id(entity.getId())
                .nmPerfil(entity.getNmPerfil())
                .aplicativo(AplicativosMapper.toDomain(entity.getAplicativo())) 
                .permissoes(entity.getPermissoes() == null ? Collections.emptySet() : 
                    entity.getPermissoes().stream().map(AuthPermissaoMapper::toDomain).collect(Collectors.toSet()))
                .build();
    }

    public static AuthPerfil toEntity(AuthPerfilModel domain) {
        if (domain == null) return null;
        AuthPerfil entity = new AuthPerfil();
        entity.setId(domain.getId());
        entity.setNmPerfil(domain.getNmPerfil());
        if (domain.getAplicativo() != null) {
            entity.setAplicativo(AplicativosMapper.toEntity(domain.getAplicativo()));
        }
        entity.setPermissoes(domain.getPermissoes() == null ? Collections.emptySet() : 
            domain.getPermissoes().stream().map(AuthPermissaoMapper::toEntity).collect(Collectors.toSet()));
        return entity;
    }

    public static AuthPerfilResponseDTO toResponse(AuthPerfilModel domain) {
        if (domain == null) return null;
        return new AuthPerfilResponseDTO(
                domain.getId(),
                domain.getNmPerfil(),
                domain.getAplicativo() != null ? domain.getAplicativo().getNmAplicativo() : null
        );
    }
}
