package br.com.projeto.piloto.domain.port.outbound;

import java.util.Optional;
import java.util.Set;

import br.com.projeto.piloto.domain.model.User;

public interface UserDomainRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    void deleteById(Long id);

    Set<User> findAllAsSet();

}
