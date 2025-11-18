package br.com.projeto.piloto.application.usecase;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;
import br.com.projeto.piloto.adapter.out.jpa.repository.RoleJpaRepository;
import br.com.projeto.piloto.domain.model.Role;
import br.com.projeto.piloto.domain.model.User;
import br.com.projeto.piloto.domain.port.inbound.UserUseCasePort;
import br.com.projeto.piloto.domain.port.outbound.UserDomainRepositoryPort;

@Service
public class UserDomainService implements UserUseCasePort {

    private final UserDomainRepositoryPort userRepository;
    private final RoleJpaRepository roleRepository;

    public UserDomainService(UserDomainRepositoryPort userRepository,
                             RoleJpaRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ------------------------------------------------------------
    // READ METHODS
    // ------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<User> findAllAsSet() {
        return userRepository.findAllAsSet();
    }

    // ------------------------------------------------------------
    // WRITE METHODS
    // ------------------------------------------------------------

    @Override
    @Transactional
    public User save(User user) {

        if (user.getPerfis() != null && !user.getPerfis().isEmpty()) {

            Set<RoleEntity> rolesFromDb =
                    user.getPerfis().stream()
                        .map(role -> roleRepository.findById(role.getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Perfil não encontrado: " + role.getId()
                            )))
                        .collect(Collectors.toSet());

            Set<Role> domainRoles =
                    rolesFromDb.stream()
                            .map(entity -> Role.builder()
                                    .id(entity.getId())
                                    .nome(entity.getNome())
                                    .build())
                            .collect(Collectors.toSet());

            user.setPerfis(domainRoles);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
