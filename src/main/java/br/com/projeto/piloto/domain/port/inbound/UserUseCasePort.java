package br.com.projeto.piloto.domain.port.inbound;

import java.util.Optional;
import java.util.Set;

import br.com.projeto.piloto.domain.model.User;

public interface UserUseCasePort {

    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);

    Set<User> findAllAsSet();

    User save(User user);

    void deleteById(Long id);
}
