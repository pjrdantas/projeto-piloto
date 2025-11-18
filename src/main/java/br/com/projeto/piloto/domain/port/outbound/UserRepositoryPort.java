package br.com.projeto.piloto.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import br.com.projeto.piloto.domain.model.User;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);
    Optional<User> findByLogin(String login);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
}
