package br.com.projeto.piloto.domain.port.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import br.com.projeto.piloto.domain.model.AuthPerfilModel;

public interface AuthPerfilRepositoryPort {

    AuthPerfilModel create(@SuppressWarnings("deprecation") @NonNull AuthPerfilModel domain);
    Optional<AuthPerfilModel> findById(Long id);    
    Optional<AuthPerfilModel> findByNmPerfil(String nome);
    
    AuthPerfilModel update(@SuppressWarnings("deprecation") @NonNull Long id, @SuppressWarnings("deprecation") @NonNull AuthPerfilModel domain);

    List<AuthPerfilModel> listAll();
    void delete(@SuppressWarnings("deprecation") @NonNull Long id);
    
    boolean existsByNmPerfil(String nmPerfil);
}
