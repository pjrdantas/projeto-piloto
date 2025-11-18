package br.com.projeto.piloto.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import br.com.projeto.piloto.domain.model.Permission;

public interface PermissionRepositoryPort {

    Optional<Permission> findById(Long id);
    Optional<Permission> findByNome(String nome);
    List<Permission> findAll();
    Permission save(Permission permission);
    void deleteById(Long id);
}
