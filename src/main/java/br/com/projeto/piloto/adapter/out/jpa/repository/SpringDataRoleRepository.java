package br.com.projeto.piloto.adapter.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;

public interface SpringDataRoleRepository extends JpaRepository<RoleEntity, Long> {
}
