package br.com.projeto.piloto.application.usecase;

import br.com.projeto.piloto.api.dto.UsuarioDTO;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.model.User;
import br.com.projeto.piloto.domain.port.outbound.UserRepositoryPort;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepositoryPort userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public UsuarioDTO createUser(UsuarioDTO dto, String senha) {

        User user = new User();
        user.setLogin(dto.login());
        user.setNome(dto.nome());
        user.setAtivo(dto.ativo().equalsIgnoreCase("S"));


        user.setSenha(passwordEncoder.encode(senha));

        if (dto.perfisIds() != null) {
            Set<Role> roles =
                    dto.perfisIds().stream()
                            .map(id -> {
                                Role r = new Role();
                                r.setId(id);
                                return r;
                            })
                            .collect(Collectors.toSet());

            user.setPerfis(roles);
        }

        User saved = userRepository.save(user);

        return toDTO(saved);
    }

    // READ
    public Optional<UsuarioDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO);
    }

    public List<UsuarioDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public Optional<UsuarioDTO> update(Long id, UsuarioDTO dto, String senha) {
        return userRepository.findById(id).map(existing -> {

            existing.setNome(dto.nome());
            existing.setLogin(dto.login());
            existing.setAtivo(dto.ativo().equalsIgnoreCase("S"));


            if (senha != null && !senha.isBlank()) {
                existing.setSenha(passwordEncoder.encode(senha));
            }

            if (dto.perfisIds() != null) {
                Set<Role> roles =
                        dto.perfisIds().stream()
                                .map(roleId -> {
                                    Role r = new Role();
                                    r.setId(roleId);
                                    return r;
                                })
                                .collect(Collectors.toSet());

                existing.setPerfis(roles);
            }

            User saved = userRepository.save(existing);

            return toDTO(saved);
        });
    }

    // DELETE
    public boolean delete(Long id) {
        return userRepository.findById(id).map(ex -> {
            userRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    // DOMAIN → DTO
    private UsuarioDTO toDTO(User u) {
        return new UsuarioDTO(
                u.getId(),
                u.getNome(),
                u.getLogin(),
                u.isAtivo() ? "S" : "N",
                u.getPerfis().stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet())
        );
    }
}
