package br.com.projeto.piloto.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.port.outbound.AplicativosRepositoryPort;
import br.com.projeto.piloto.domain.port.outbound.AuthPerfilRepositoryPort;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthPerfilInteractorTest {

    @Mock private AuthPerfilRepositoryPort repository;
    @Mock private AplicativosRepositoryPort aplicativosRepository;
    @InjectMocks private AuthPerfilInteractor service;

    @SuppressWarnings("null")
	@Test
    @DisplayName("Erro se nome de perfil já existe")
    void createNomeExiste() {
        AuthPerfilModel domain = AuthPerfilModel.builder().nmPerfil("EXISTE").build();
        when(repository.existsByNmPerfil("EXISTE")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(domain));

        verify(repository, never()).create(any());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Sucesso ao criar perfil")
    void createSucessoTotal() {
        AuthPerfilModel domain = AuthPerfilModel.builder()
                .nmPerfil("NOVO")
                .build();

        when(repository.existsByNmPerfil("NOVO")).thenReturn(false);

        when(repository.create(any())).thenReturn(domain);

        AuthPerfilModel result = service.create(domain);
        
        assertNotNull(result);
        verify(repository).create(any());
    }

    @Test
    @DisplayName("Sucesso ao deletar perfil")
    void deleteSucesso() {
 
        when(repository.findById(1L)).thenReturn(Optional.of(AuthPerfilModel.builder().id(1L).build()));
        
        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository).delete(1L);
    }
    
    @Test
    @DisplayName("Deve validar o Builder e o valor Default do Set")
    void deveValidarBuilderEDefault() {
        AuthPerfilModel model = AuthPerfilModel.builder()
                .id(1L)
                .nmPerfil("USER")
                .build();

        assertNotNull(model);
        assertEquals("USER", model.getNmPerfil());
        assertNotNull(model.getPermissoes());  
        assertTrue(model.getPermissoes().isEmpty());
    }

}
