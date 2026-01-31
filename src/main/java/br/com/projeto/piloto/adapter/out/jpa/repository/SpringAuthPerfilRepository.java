package br.com.projeto.piloto.adapter.out.jpa.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;

@Repository
public interface SpringAuthPerfilRepository extends JpaRepository<AuthPerfil, Long> {

    // Necessário para validar se o nome já existe antes de criar/atualizar
    boolean existsByNmPerfil(String nmPerfil);

    // Necessário para o método findByNmPerfil que estava retornando Optional.empty() no seu Adapter
    Optional<AuthPerfil> findByNmPerfil(String nmPerfil);
}
