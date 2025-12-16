package br.com.projeto.piloto.application.usecase;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.projeto.piloto.adapter.out.jpa.entity.AuthUsuario;
import br.com.projeto.piloto.adapter.out.jpa.mapper.AuthUsuarioMapper;
import br.com.projeto.piloto.domain.exception.UserNotFoundException;
import br.com.projeto.piloto.domain.model.AuthUsuarioModel;
import br.com.projeto.piloto.domain.port.inbound.AuthUsuarioUseCasePort;
import br.com.projeto.piloto.domain.port.outbound.AuthUsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUsuarioService implements AuthUsuarioUseCasePort {

    private final AuthUsuarioRepositoryPort repository;
    private final AuthUsuarioMapper mapper;

    @Override
    public AuthUsuarioModel criar(AuthUsuarioModel model) {
        validar(model);

        if (repository.findByLogin(model.getLogin()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este login.");
        }

        AuthUsuario saved = repository.save(mapper.toEntity(model));
        return mapper.toDomain(saved);
    }

    @Override
    public AuthUsuarioModel atualizar(Long id, AuthUsuarioModel model) {
        validar(model);

        AuthUsuario existing = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));

        if (repository.existsByDsLoginAndIdNot(model.getLogin(), id)) {
            throw new DataIntegrityViolationException("Já existe um usuário com este login.");
        }

        existing.setLogin(model.getLogin());
        existing.setSenha(model.getSenha());
        existing.setNome(model.getNome());
        existing.setAtivo(model.getAtivo());
        existing.setEmail(model.getEmail());
        existing.setPerfis(mapper.toEntity(model).getPerfis());

        return mapper.toDomain(repository.save(existing));
    }

    @Override
    public void deletar(Long id) {
        AuthUsuario usuario = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
        repository.deleteById(usuario.getId());
    }

    @Override
    public AuthUsuarioModel buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }

    @Override
    public List<AuthUsuarioModel> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    private void validar(AuthUsuarioModel model) {
        Objects.requireNonNull(model);
        if (model.getPerfis() == null || model.getPerfis().isEmpty()) {
            throw new IllegalArgumentException("Usuário deve possuir ao menos um perfil.");
        }
    }
}