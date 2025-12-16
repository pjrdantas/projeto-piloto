package br.com.projeto.piloto.adapter.out.jpa.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthUsuario;
import br.com.projeto.piloto.domain.model.AuthUsuarioModel;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;

@Component
public class AuthUsuarioMapper {

    public AuthUsuarioModel toDomain(AuthUsuario usuario) {
        if (usuario == null) return null;

        return AuthUsuarioModel.builder()
                .id(usuario.getId())
                .login(usuario.getLogin())
                .senha(usuario.getSenha())
                .nome(usuario.getNome())
                .ativo(usuario.getAtivo())
                .email(usuario.getEmail())
                .criadoEm(usuario.getCriadoEm())
                .atualizadoEm(usuario.getAtualizadoEm())
                .perfis(
                    usuario.getPerfis().stream()
                        .map(this::toDomainPerfil)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public AuthUsuario toEntity(AuthUsuarioModel model) {
        if (model == null) return null;

        AuthUsuario usuario = new AuthUsuario();
        usuario.setId(model.getId());
        usuario.setLogin(model.getLogin());
        usuario.setSenha(model.getSenha());
        usuario.setNome(model.getNome());
        usuario.setAtivo(model.getAtivo());
        usuario.setEmail(model.getEmail());

        usuario.setPerfis(
            model.getPerfis().stream()
                .map(this::toEntityPerfil)
                .collect(Collectors.toSet())
        );

        return usuario;
    }

    private AuthPerfilModel toDomainPerfil(AuthPerfil p) {
        return AuthPerfilModel.builder()
                .id(p.getId())
                .nmPerfil(p.getNmPerfil())
                .build();
    }

    private AuthPerfil toEntityPerfil(AuthPerfilModel model) {
        AuthPerfil p = new AuthPerfil();
        p.setId(model.getId());
        return p; // ⚠️ só ID, JPA resolve
    }
}