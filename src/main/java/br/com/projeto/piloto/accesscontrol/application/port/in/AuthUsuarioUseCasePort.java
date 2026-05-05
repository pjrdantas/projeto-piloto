package br.com.projeto.piloto.accesscontrol.application.port.in;

import java.util.List;

import br.com.projeto.piloto.accesscontrol.domain.model.AuthUsuarioModel;

public interface AuthUsuarioUseCasePort {

    // Contrato canônico (português - padrão atual do projeto)
    AuthUsuarioModel criar(AuthUsuarioModel model);

    AuthUsuarioModel atualizar(Long id, AuthUsuarioModel model);

    void deletar(Long id);

    AuthUsuarioModel buscarPorId(Long id);

    AuthUsuarioModel buscarPorLogin(String login);

    List<AuthUsuarioModel> listarTodos();

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    // Alias para compatibilizar nomenclatura (evita manter contratos paralelos)
    default AuthUsuarioModel create(AuthUsuarioModel usuario) {
        return criar(usuario);
    }

    default AuthUsuarioModel update(Long id, AuthUsuarioModel usuario) {
        return atualizar(id, usuario);
    }

    default AuthUsuarioModel findById(Long id) {
        return buscarPorId(id);
    }

    default AuthUsuarioModel findByUsername(String username) {
        return buscarPorLogin(username);
    }

    default List<AuthUsuarioModel> listAll() {
        return listarTodos();
    }

    default void delete(Long id) {
        deletar(id);
    }

    default boolean existsByUsername(String username) {
        return existsByLogin(username);
    }
}
