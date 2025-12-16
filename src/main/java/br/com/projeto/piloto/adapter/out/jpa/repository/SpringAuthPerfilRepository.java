package br.com.projeto.piloto.adapter.out.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.projeto.piloto.adapter.out.jpa.entity.AuthPerfil;

@Repository
public interface SpringAuthPerfilRepository extends JpaRepository<AuthPerfil, Long> {
	Optional<AuthPerfil> findByNmPerfil(String nmPerfil);
	
	boolean existsByNmPerfil(String nmPerfil);
	
}
