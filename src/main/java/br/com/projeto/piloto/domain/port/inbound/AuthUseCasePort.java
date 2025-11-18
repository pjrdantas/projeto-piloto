package br.com.projeto.piloto.domain.port.inbound;

import br.com.projeto.piloto.domain.model.User;

public interface AuthUseCasePort {

    User authenticate(String login, String senha);

    User register(User user);
}
