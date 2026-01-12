package br.com.projeto.piloto.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.port.inbound.AuthPerfilUseCase;
import br.com.projeto.piloto.domain.port.outbound.AuthPerfilRepositoryPort;
import br.com.projeto.piloto.domain.port.outbound.AplicativosRepositoryPort;  
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthPerfilInteractor implements AuthPerfilUseCase {

    private final AuthPerfilRepositoryPort repository;
    private final AplicativosRepositoryPort aplicativosRepository; 

    @Override
    @Transactional
    public AuthPerfilModel create(AuthPerfilModel domain) {
        Objects.requireNonNull(domain, "O modelo de perfil não pode ser nulo");

        if (repository.existsByNmPerfil(domain.getNmPerfil())) {
            throw new IllegalArgumentException("Perfil já existe: " + domain.getNmPerfil());
        }

        if (domain.getAplicativo() == null || domain.getAplicativo().getId() == null) {
            throw new IllegalArgumentException("Todo perfil deve estar vinculado a um Aplicativo válido.");
        }

        AplicativosModel app = aplicativosRepository.findById(domain.getAplicativo().getId())
            .orElseThrow(() -> new IllegalArgumentException("Aplicativo informado não encontrado: ID " + domain.getAplicativo().getId()));

        domain.setAplicativo(app); 

        return repository.create(domain);
    }

    @Override
    @Transactional
    public AuthPerfilModel update(Long id, AuthPerfilModel domain) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        Objects.requireNonNull(domain, "O modelo de perfil não pode ser nulo");

        repository.findByNmPerfil(domain.getNmPerfil())
            .filter(p -> !p.getId().equals(id))
            .ifPresent(p -> {
                throw new IllegalArgumentException("Já existe outro perfil com este nome: " + domain.getNmPerfil());
            });

        if (domain.getAplicativo() != null && domain.getAplicativo().getId() != null) {
            AplicativosModel app = aplicativosRepository.findById(domain.getAplicativo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aplicativo não encontrado."));
            domain.setAplicativo(app);
        }

        return repository.update(id, domain);
    }
    
    @Override
    public Optional<AuthPerfilModel> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<AuthPerfilModel> listAll() {
        return repository.listAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID para exclusão não pode ser nulo");
        }

        repository.delete(id);
    }

    @Override
    public boolean existsByNmPerfil(String nmPerfil) {
        return repository.existsByNmPerfil(nmPerfil);
    }
}
