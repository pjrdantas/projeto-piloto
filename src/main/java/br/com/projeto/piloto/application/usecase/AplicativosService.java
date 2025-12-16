package br.com.projeto.piloto.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.port.inbound.AplicativosUseCase;
import br.com.projeto.piloto.domain.port.outbound.AplicativosRepositoryPort;

@Service
public class AplicativosService implements AplicativosUseCase {

    private final AplicativosRepositoryPort repository;

    public AplicativosService(AplicativosRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<AplicativosModel> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<AplicativosModel> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public AplicativosModel create(AplicativosModel APplicativos) {
        APplicativos.setCriadoEm(LocalDateTime.now());
        APplicativos.setAtualizadoEm(LocalDateTime.now());
        return repository.save(APplicativos);
    }

    @Override
    public AplicativosModel update(Long id, AplicativosModel APplicativos) {
        Optional<AplicativosModel> existing = repository.findById(id);

        if (existing.isEmpty()) {
            throw new IllegalArgumentException("APplicativos não encontrado: " + id);
        }

        AplicativosModel m = existing.get();
        m.setNome(APplicativos.getNome());
        m.setDescricao(APplicativos.getDescricao());
        m.setUrl(APplicativos.getUrl());
        m.setModuleName(APplicativos.getModuleName());
        m.setExposedModule(APplicativos.getExposedModule());
        m.setRoutePath(APplicativos.getRoutePath());
        m.setAtualizadoEm(LocalDateTime.now());

        return repository.save(m);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByNome(String nome) {
        return repository.existsByNome(nome);
    }

    @Override
    public List<AplicativosModel> listAtivos() {
        return repository.findByAtivo("S");
    }

}
