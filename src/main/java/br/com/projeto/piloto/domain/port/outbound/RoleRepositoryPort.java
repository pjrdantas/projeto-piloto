package br.com.projeto.piloto.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import br.com.projeto.piloto.domain.model.Role;

public interface RoleRepositoryPort {

    Optional<Role> findById(Long id);
    List<Role> findAll();
    Role save(Role role);
    void deleteById(Long id);
}
