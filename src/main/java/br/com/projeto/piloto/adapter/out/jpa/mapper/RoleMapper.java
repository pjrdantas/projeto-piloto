package br.com.projeto.piloto.adapter.out.jpa.mapper;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;
import br.com.projeto.piloto.domain.model.Role;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        if (entity == null) return null;

        return Role.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

    public RoleEntity toEntity(Role role) {
        if (role == null) return null;

        return RoleEntity.builder()
                .id(role.getId())
                .nome(role.getNome())
                .build();
    }
}
