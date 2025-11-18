package br.com.projeto.piloto.adapter.out.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.repository.RoleJpaRepository;
import br.com.projeto.piloto.adapter.out.jpa.mapper.RoleMapper;
import br.com.projeto.piloto.adapter.out.jpa.mapper.PermissionMapper;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.port.outbound.RoleRepositoryPort;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository,
                                 RoleMapper roleMapper,
                                 PermissionMapper permissionMapper) {
        this.roleJpaRepository = roleJpaRepository;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll()
                .stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Role save(Role role) {
        // converte para entity incluindo permissões
        return roleMapper.toDomain(
                roleJpaRepository.save(roleMapper.toEntity(role))
        );
    }

    @Override
    public void deleteById(Long id) {
        roleJpaRepository.deleteById(id);
    }
}
