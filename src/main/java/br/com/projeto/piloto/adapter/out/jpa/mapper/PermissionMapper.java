package br.com.projeto.piloto.adapter.out.jpa.mapper;

import org.springframework.stereotype.Component;
import br.com.projeto.piloto.adapter.out.jpa.entity.PermissionEntity;
import br.com.projeto.piloto.domain.model.Permission;

@Component
public class PermissionMapper {

    public Permission toDomain(PermissionEntity entity) {
        if (entity == null) return null;
        return Permission.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) return null;
        return PermissionEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .build();
    }
}
