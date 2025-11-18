package br.com.projeto.piloto.adapter.out.jpa.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;
import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.model.User;

@Component
public class UserDTOMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .ativo("S".equalsIgnoreCase(entity.getAtivo()))
                .perfis(
                    entity.getPerfis()
                        .stream()
                        .map(role ->
                                Role.builder()
                                        .id(role.getId())
                                        .nome(role.getNome())
                                        .build()
                        )
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;

        UserEntity entity = UserEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .login(domain.getLogin())
                .senha(domain.getSenha())
                .ativo(domain.isAtivo() ? "S" : "N")
                .build();

        if (domain.getPerfis() != null) {
            entity.setPerfis(
                domain.getPerfis()
                      .stream()
                      .map(role ->
                              RoleEntity.builder()
                                      .id(role.getId())
                                      .nome(role.getNome())
                                      .build()
                      )
                      .collect(Collectors.toSet())
            );
        }

        return entity;
    }
}
