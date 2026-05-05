package br.com.projeto.piloto.accesscontrol.application.port.in;



import java.util.List;

import br.com.projeto.piloto.accesscontrol.domain.model.AuthUsuarioModel;

public interface AuthUsuarioUseCasePort {

    AuthUsuarioModel criar(AuthUsuarioModel model);

    AuthUsuarioModel atualizar(Long id, AuthUsuarioModel model);

    void deletar(Long id);

    AuthUsuarioModel buscarPorId(Long id);

    AuthUsuarioModel buscarPorLogin(String login);

    List<AuthUsuarioModel> listarTodos();

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
