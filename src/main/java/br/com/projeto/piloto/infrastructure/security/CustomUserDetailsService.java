package br.com.projeto.piloto.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.adapter.out.jpa.repository.SpringDataUserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SpringDataUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // Mantemos a lógica antiga: UserEntity.ativo é String "S"/"N"
        if (!"S".equalsIgnoreCase(userEntity.getAtivo())) {
            throw new DisabledException("Usuário está inativo no sistema");
        }

        String[] authorities = userEntity.getPerfis()
                .stream()
                .map(p -> p.getNome())
                .toArray(String[]::new);

        return User.withUsername(userEntity.getLogin())
                .password(userEntity.getSenha())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
