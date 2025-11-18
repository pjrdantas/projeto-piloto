package br.com.projeto.piloto.adapter.out.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.adapter.out.jpa.mapper.PermissionMapper;
import br.com.projeto.piloto.adapter.out.jpa.repository.PermissionJpaRepository;
import br.com.projeto.piloto.domain.model.Permission;
import br.com.projeto.piloto.domain.port.outbound.PermissionRepositoryPort;

@Component
public class PermissionRepositoryAdapter implements PermissionRepositoryPort {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionMapper permissionMapper;

    public PermissionRepositoryAdapter(PermissionJpaRepository permissionJpaRepository,
                                       PermissionMapper permissionMapper) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id)
                .map(permissionMapper::toDomain);
    }

    @Override
    public Optional<Permission> findByNome(String nome) {
        return permissionJpaRepository.findByNome(nome)
                .map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll()
                .stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Permission save(Permission permission) {
        return permissionMapper.toDomain(
                permissionJpaRepository.save(permissionMapper.toEntity(permission))
        );
    }

    @Override
    public void deleteById(Long id) {
        permissionJpaRepository.deleteById(id);
    }
}
