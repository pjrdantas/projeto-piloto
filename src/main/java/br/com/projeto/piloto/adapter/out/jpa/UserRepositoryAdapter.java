package br.com.projeto.piloto.adapter.out.jpa;

import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.adapter.out.jpa.mapper.UserMapper;
import br.com.projeto.piloto.adapter.out.jpa.repository.SpringDataUserRepository;
import br.com.projeto.piloto.domain.model.User;
import br.com.projeto.piloto.domain.port.outbound.UserDomainRepositoryPort;
import br.com.projeto.piloto.domain.port.outbound.UserRepositoryPort;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter 
        implements UserRepositoryPort, UserDomainRepositoryPort {

    private final SpringDataUserRepository repository;
    private final UserMapper mapper;

    public UserRepositoryAdapter(SpringDataUserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login)
                .map(mapper::toDomain);
    }

    /** Implementação para UserRepositoryPort (List) */
    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** Implementação para UserDomainRepositoryPort (Set) */
    @Override
    public Set<User> findAllAsSet() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
