package br.com.projeto.piloto.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projeto.piloto.adapter.out.jpa.entity.AuthUsuario;
import br.com.projeto.piloto.domain.port.outbound.AuthUsuarioRepositoryPort;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUsuarioRepositoryPort userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUsuario authUsuario = userRepository.findByLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado: " + username));

        if (!"S".equalsIgnoreCase(authUsuario.getAtivo())) {
            throw new DisabledException("Usuário está inativo no sistema");
        }

        String[] authorities = authUsuario.getPerfis()
                .stream()
                .map(perfil -> "ROLE_" + perfil.getNmPerfil().toUpperCase())
                .toArray(String[]::new);

        return User.withUsername(authUsuario.getLogin())
                .password(authUsuario.getSenha())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
