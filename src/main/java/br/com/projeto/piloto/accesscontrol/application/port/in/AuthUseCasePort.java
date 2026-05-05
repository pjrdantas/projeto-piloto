package br.com.projeto.piloto.accesscontrol.application.port.in;

import br.com.projeto.piloto.accesscontrol.domain.model.AuthUsuarioModel;

public interface AuthUseCasePort {

    /**
     * Responsável apenas por autenticar usuário no sistema.
     */
    AuthUsuarioModel authenticate(String login, String senha);

    /**
     * Busca usuário por login para validações de sessão/autorização.
     */
    AuthUsuarioModel findByLogin(String login);
}