package br.com.projeto.piloto.adapter.out.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;
import br.com.projeto.piloto.adapter.out.jpa.mapper.AuthPerfilMapper;
import br.com.projeto.piloto.adapter.out.jpa.repository.SpringAuthPerfilRepository;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.port.outbound.AuthPerfilRepositoryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthPerfilRepositoryAdapter implements AuthPerfilRepositoryPort {

    private final SpringAuthPerfilRepository repository;

    @Override
    public @NonNull List<AuthPerfilModel> listAll() {
        List<AuthPerfilModel> list = repository.findAll().stream()
                .map(AuthPerfilMapper::toDomain)
                .collect(Collectors.toList());
        return Objects.requireNonNull(list, "A lista de perfis não pode ser nula");
    }

    @Override
    public Optional<AuthPerfilModel> findById(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        Optional<AuthPerfilModel> result = repository.findById(id)
                .map(AuthPerfilMapper::toDomain);
        return Objects.requireNonNull(result, "Resultado não pode ser nulo");
    }

    @Override
    public AuthPerfilModel create(@NonNull AuthPerfilModel domain) {
        Objects.requireNonNull(domain, "AuthPerfilModel não pode ser nulo");

        AuthPerfil entity = Objects.requireNonNull(AuthPerfilMapper.toEntity(domain), "AuthPerfil convertido não pode ser nulo");

        AuthPerfil saved = Objects.requireNonNull(repository.save(entity), "Perfil criado não pode ser nulo");

        return Objects.requireNonNull(AuthPerfilMapper.toDomain(saved), "Perfil criado não pode ser nulo");
    }

    @Override
    public AuthPerfilModel update(@NonNull Long id, @NonNull AuthPerfilModel domain) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        Objects.requireNonNull(domain, "AuthPerfilModel não pode ser nulo");

        return repository.findById(id)
                .map(existing -> {
                    existing.setNmPerfil(domain.getNmPerfil());
                    AuthPerfil updated = repository.save(existing);
                    return Objects.requireNonNull(AuthPerfilMapper.toDomain(updated), "Perfil atualizado não pode ser nulo");
                })
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado: " + id));
    }

    @Override
    public void delete(@NonNull Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        repository.deleteById(id);
    }

	@Override
	public boolean existsByNmPerfil(String nmPerfil) {
		 
		return repository.existsByNmPerfil(nmPerfil);
	}

	@Override
	public Optional<AuthPerfilModel> findByNmPerfil(String nome) {
		return Optional.empty();
	}
}
