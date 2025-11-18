package br.com.projeto.piloto.application.usecase;

import br.com.projeto.piloto.api.dto.RoleDTO;
import br.com.projeto.piloto.domain.model.Permission;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.port.outbound.PermissionRepositoryPort;
import br.com.projeto.piloto.domain.port.outbound.RoleRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepositoryPort roleRepository;
    private final PermissionRepositoryPort permissionRepository;

    public RoleService(RoleRepositoryPort roleRepository, PermissionRepositoryPort permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // CREATE
    public RoleDTO create(RoleDTO dto) {
        Role role = new Role();
        role.setNome(dto.nome());

        // Vincular permissões se existirem
        if (dto.permissoes() != null) {
            Set<Permission> permissions = dto.permissoes().stream()
                    .map(nome -> permissionRepository.findByNome(nome)
                            .orElseThrow(() -> new IllegalArgumentException("Permissão não encontrada: " + nome)))
                    .collect(Collectors.toSet());
            role.setPermissoes(permissions);
        }

        Role saved = roleRepository.save(role);
        return toDTO(saved);
    }

    // READ
    public Optional<RoleDTO> findById(Long id) {
        return roleRepository.findById(id)
                .map(this::toDTO);
    }

    public List<RoleDTO> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public Optional<RoleDTO> update(Long id, RoleDTO dto) {
        return roleRepository.findById(id).map(existing -> {
            existing.setNome(dto.nome());

            // Atualizar permissões
            if (dto.permissoes() != null) {
                Set<Permission> permissions = dto.permissoes().stream()
                        .map(nome -> permissionRepository.findByNome(nome)
                                .orElseThrow(() -> new IllegalArgumentException("Permissão não encontrada: " + nome)))
                        .collect(Collectors.toSet());
                existing.setPermissoes(permissions);
            }

            Role saved = roleRepository.save(existing);
            return toDTO(saved);
        });
    }

    // DELETE
    public boolean delete(Long id) {
        return roleRepository.findById(id).map(role -> {
            roleRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    // DOMAIN → DTO
    private RoleDTO toDTO(Role role) {
        Set<String> permissionNames = role.getPermissoes() != null
                ? role.getPermissoes().stream().map(Permission::getNome).collect(Collectors.toSet())
                : null;

        return new RoleDTO(
                role.getId(),
                role.getNome(),
                permissionNames
        );
    }
}
