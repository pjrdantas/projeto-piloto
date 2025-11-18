package br.com.projeto.piloto.application.usecase;

import br.com.projeto.piloto.api.dto.PermissionDTO;
import br.com.projeto.piloto.domain.model.Permission;
import br.com.projeto.piloto.domain.port.outbound.PermissionRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepositoryPort permissionRepository;

    public PermissionService(PermissionRepositoryPort permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // CREATE
    public PermissionDTO create(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setNome(dto.nome());

        Permission saved = permissionRepository.save(permission);

        return toDTO(saved);
    }

    // READ
    public Optional<PermissionDTO> findById(Long id) {
        return permissionRepository.findById(id)
                .map(this::toDTO);
    }

    public List<PermissionDTO> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public Optional<PermissionDTO> update(Long id, PermissionDTO dto) {
        return permissionRepository.findById(id)
                .map(existing -> {
                    existing.setNome(dto.nome());
                    Permission saved = permissionRepository.save(existing);
                    return toDTO(saved);
                });
    }

    // DELETE
    public boolean delete(Long id) {
        return permissionRepository.findById(id)
                .map(p -> {
                    permissionRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    // DOMAIN → DTO
    private PermissionDTO toDTO(Permission p) {
        return new PermissionDTO(
                p.getId(),
                p.getNome()
        );
    }
}
