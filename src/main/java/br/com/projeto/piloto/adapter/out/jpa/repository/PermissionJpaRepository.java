package br.com.projeto.piloto.adapter.out.jpa.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.projeto.piloto.adapter.out.jpa.entity.PermissionEntity;

public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByNome(String nome);
}
