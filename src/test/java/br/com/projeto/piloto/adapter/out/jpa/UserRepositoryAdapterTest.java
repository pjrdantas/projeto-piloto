package br.com.projeto.piloto.adapter.out.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.projeto.piloto.adapter.out.jpa.entity.UserEntity;
import br.com.projeto.piloto.adapter.out.jpa.mapper.UserMapper;
import br.com.projeto.piloto.adapter.out.jpa.repository.SpringDataUserRepository;
import br.com.projeto.piloto.domain.model.User;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private SpringDataUserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void deveDelegarFindByLogin_quandoUsuarioExistir() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .login("usuario1")
                .build();

        User domainUser = User.builder()
                .id(1L)
                .login("usuario1")
                .build();

        when(repository.findByLogin("usuario1")).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(domainUser);

        Optional<User> resultado = adapter.findByLogin("usuario1");

        assertTrue(resultado.isPresent());
        assertSame(domainUser, resultado.get());

        verify(repository, times(1)).findByLogin("usuario1");
        verify(mapper, times(1)).toDomain(userEntity);
    }

    @Test
    void deveRetornarEmpty_quandoUsuarioNaoExistir() {
        when(repository.findByLogin("naoexiste")).thenReturn(Optional.empty());

        Optional<User> resultado = adapter.findByLogin("naoexiste");

        assertFalse(resultado.isPresent());
        verify(repository, times(1)).findByLogin("naoexiste");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void deveDelegarSave_eRetornarUsuarioSalvo() {
        User domainBefore = User.builder()
                .login("novo")
                .nome("Novo")
                .build();

        UserEntity entityBefore = UserEntity.builder()
                .login("novo")
                .nome("Novo")
                .build();

        UserEntity entitySaved = UserEntity.builder()
                .id(10L)
                .login("novo")
                .nome("Novo")
                .build();

        User domainSaved = User.builder()
                .id(10L)
                .login("novo")
                .nome("Novo")
                .build();

        when(mapper.toEntity(domainBefore)).thenReturn(entityBefore);
        when(repository.save(entityBefore)).thenReturn(entitySaved);
        when(mapper.toDomain(entitySaved)).thenReturn(domainSaved);

        User resultado = adapter.save(domainBefore);

        assertSame(domainSaved, resultado);

        verify(mapper, times(1)).toEntity(domainBefore);
        verify(repository, times(1)).save(entityBefore);
        verify(mapper, times(1)).toDomain(entitySaved);
    }
}
