package br.com.projeto.piloto.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.projeto.piloto.adapter.out.jpa.entity.RoleEntity;
import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.adapter.out.jpa.repository.SpringDataUserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private SpringDataUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void loadUserByUsername_deveRetornarUserDetails_quandoUsuarioExistirEAtivo() {
        RoleEntity r1 = RoleEntity.builder().id(1L).nome("ROLE_ADMIN").build();
        RoleEntity r2 = RoleEntity.builder().id(2L).nome("ROLE_USER").build();

        UserEntity userEntity = UserEntity.builder()
                .id(10L)
                .login("usuario1")
                .senha("senhaCodificada")
                .ativo("S")
                .perfis(Set.of(r1, r2))
                .build();

        when(userRepository.findByLogin("usuario1")).thenReturn(Optional.of(userEntity));

        UserDetails ud = service.loadUserByUsername("usuario1");

        assertEquals("usuario1", ud.getUsername());
        assertEquals("senhaCodificada", ud.getPassword());

        Set<String> authorities = ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("ROLE_USER"));
    }

    @Test
    void loadUserByUsername_deveLancarUsernameNotFound_quandoUsuarioNaoExistir() {
        when(userRepository.findByLogin("naoexiste")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("naoexiste"));
    }

    @Test
    void loadUserByUsername_deveLancarDisabledException_quandoUsuarioInativo() {
        UserEntity userEntity = UserEntity.builder()
                .login("inativo")
                .senha("x")
                .ativo("N")
                .build();

        when(userRepository.findByLogin("inativo")).thenReturn(Optional.of(userEntity));

        assertThrows(DisabledException.class,
                () -> service.loadUserByUsername("inativo"));
    }
}
