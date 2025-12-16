package br.com.projeto.piloto.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.port.inbound.AuthPerfilUseCase;
import br.com.projeto.piloto.domain.port.outbound.AuthPerfilRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthPerfilService implements AuthPerfilUseCase {

    private final AuthPerfilRepositoryPort repository;

    @Override
    public AuthPerfilModel create(AuthPerfilModel domain) {
        Objects.requireNonNull(domain);

        if (repository.existsByNmPerfil(domain.getNmPerfil())) {
            throw new IllegalArgumentException("Perfil já existe: " + domain.getNmPerfil());
        }

        return repository.create(domain);
    }

    @Override
    public AuthPerfilModel update(Long id, AuthPerfilModel domain) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(domain);

        repository.findByNmPerfil(domain.getNmPerfil())
            .filter(p -> !p.getId().equals(id))
            .ifPresent(p -> {
                throw new IllegalArgumentException("Perfil já existe: " + domain.getNmPerfil());
            });

        return repository.update(id, domain);
    }
    
    
    @Override
    public Optional<AuthPerfilModel> findById(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        Optional<AuthPerfilModel> result = repository.findById(id);
        return Objects.requireNonNull(result, "Resultado não pode ser nulo");
    }

    @Override
    public List<AuthPerfilModel> listAll() {
        List<AuthPerfilModel> list = repository.listAll();
        return Objects.requireNonNull(list, "A lista de perfis não pode ser nula");
    }



    @Override
    public void delete(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        repository.delete(id);
    }

	@Override
	public boolean existsByNmPerfil(String nmPerfil) {
		
		return repository.existsByNmPerfil(nmPerfil);
	}
}
