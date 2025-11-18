package br.com.projeto.piloto.adapter.out.jpa.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;
import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.model.User;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .ativo("S".equalsIgnoreCase(entity.getAtivo()))
                .perfis(
                    entity.getPerfis().stream()
                        .map(role -> Role.builder()
                                .id(role.getId())
                                .nome(role.getNome())
                                .build()
                        ).collect(Collectors.toSet())
                )
                .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .nome(user.getNome())
                .login(user.getLogin())
                .senha(user.getSenha())
                .ativo(user.isAtivo() ? "S" : "N")
                .build();

        if (user.getPerfis() != null) {
            entity.setPerfis(
                user.getPerfis().stream()
                    .map(role -> RoleEntity.builder()
                            .id(role.getId())
                            .nome(role.getNome())
                            .build()
                    ).collect(Collectors.toSet())
            );
        }

        return entity;
    }
}
