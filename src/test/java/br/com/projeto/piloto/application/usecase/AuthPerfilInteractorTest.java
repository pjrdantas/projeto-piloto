package br.com.projeto.piloto.application.usecase;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.port.outbound.AplicativosRepositoryPort;
import br.com.projeto.piloto.domain.port.outbound.AuthPerfilRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AuthPerfilInteractorTest {

    @Mock private AuthPerfilRepositoryPort repository;
    @Mock private AplicativosRepositoryPort aplicativosRepository;
    @InjectMocks private AuthPerfilInteractor service;

    @Test
    @DisplayName("Cobre Linha 30 (Amarela) e 31 (Vermelha): Erro se nome já existe")
    void createNomeExiste() {
        AuthPerfilModel domain = AuthPerfilModel.builder().nmPerfil("EXISTE").build();
        when(repository.existsByNmPerfil("EXISTE")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(domain));
    }

    @Test
    @DisplayName("Cobre Linha 41 (Amarela) e 42: Erro se App informado não existe no banco")
    void createAppNaoEncontrado() {
        AplicativosModel app = AplicativosModel.builder().id(99L).build();
        AuthPerfilModel domain = AuthPerfilModel.builder().nmPerfil("NOVO").aplicativo(app).build();

        when(repository.existsByNmPerfil("NOVO")).thenReturn(false);
        when(aplicativosRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.create(domain));
    }

    @Test
    @DisplayName("Cobre Linha 56 (Amarela) e 58 (Vermelha): Nome duplicado em ID diferente no Update")
    void updateNomeDuplicadoOutroId() {
        Long id = 1L;
        AuthPerfilModel outro = AuthPerfilModel.builder().id(2L).nmPerfil("ADMIN").build();
        AuthPerfilModel domain = AuthPerfilModel.builder().id(id).nmPerfil("ADMIN").build();

        when(repository.findByNmPerfil("ADMIN")).thenReturn(Optional.of(outro));

        assertThrows(IllegalArgumentException.class, () -> service.update(id, domain));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 62 (Amarela) e 63, 64, 65 (Vermelhas): Atualização mudando Aplicativo")
    void updateMudandoAplicativoSucesso() {
        Long id = 1L;
        AplicativosModel novoApp = AplicativosModel.builder().id(10L).build();
        AuthPerfilModel domain = AuthPerfilModel.builder().id(id).nmPerfil("NOME").aplicativo(novoApp).build();

        when(repository.findByNmPerfil(any())).thenReturn(Optional.empty());
        when(aplicativosRepository.findById(10L)).thenReturn(Optional.of(novoApp));
        when(repository.update(eq(id), any())).thenReturn(domain);

        AuthPerfilModel result = service.update(id, domain);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Cobre Linha 64 (Vermelha): Erro se novo aplicativo não existir no Update")
    void updateAppNaoEncontrado() {
        Long id = 1L;
        AplicativosModel appInexistente = AplicativosModel.builder().id(99L).build();
        AuthPerfilModel domain = AuthPerfilModel.builder().nmPerfil("NOME").aplicativo(appInexistente).build();

        when(repository.findByNmPerfil(any())).thenReturn(Optional.empty());
        when(aplicativosRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.update(id, domain));
    }

    @Test
    @DisplayName("Cobre Linha 73 e 78 (Vermelhas): findById e listAll")
    void findAndList() {
        when(repository.findById(1L)).thenReturn(Optional.of(new AuthPerfilModel()));
        when(repository.listAll()).thenReturn(List.of(new AuthPerfilModel()));

        assertTrue(service.findById(1L).isPresent());
        assertFalse(service.listAll().isEmpty());
    }

    @Test
    @DisplayName("Cobre Linha 85 (Amarela) e 86 (Vermelha): Erro ao deletar com ID nulo")
    void deleteIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
    }

    @Test
    @DisplayName("Cobre Linha 95 (Vermelha - caso exista): existsByNmPerfil")
    void existsByNmPerfil() {
        when(repository.existsByNmPerfil("TESTE")).thenReturn(true);
        assertTrue(service.existsByNmPerfil("TESTE"));
    }
    
    @Test
    @DisplayName("Cobre Linha 35 (Amarela) e 36 (Vermelha): App nulo ou ID nulo no Create")
    void createValidacaoApp() {
        AuthPerfilModel d1 = AuthPerfilModel.builder().nmPerfil("P1").aplicativo(null).build();
        assertThrows(IllegalArgumentException.class, () -> service.create(d1));
        AuthPerfilModel d2 = AuthPerfilModel.builder().nmPerfil("P1")
                .aplicativo(AplicativosModel.builder().id(null).build()).build();
        assertThrows(IllegalArgumentException.class, () -> service.create(d2));
    }

    @Test
    @DisplayName("Cobre Linha 40 (Amarela) e 41 (Amarela) e 43 (Vermelha): App inexistente no Create")
    void createNotFoundApp() {
        Long appId = 99L;
        AuthPerfilModel domain = AuthPerfilModel.builder()
                .nmPerfil("P1")
                .aplicativo(AplicativosModel.builder().id(appId).build())
                .build(); 

        when(repository.existsByNmPerfil(any())).thenReturn(false);
        when(aplicativosRepository.findById(appId)).thenReturn(Optional.empty()); 
        assertThrows(IllegalArgumentException.class, () -> service.create(domain));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 56 (Amarela): Update com mesmo nome e mesmo ID")
    void updateMesmoNomeMesmoId() {
        Long id = 1L;
        AuthPerfilModel domain = AuthPerfilModel.builder().id(id).nmPerfil("ADMIN").build();
        when(repository.findByNmPerfil("ADMIN")).thenReturn(Optional.of(domain));
        when(repository.update(eq(id), any())).thenReturn(domain);

        assertDoesNotThrow(() -> service.update(id, domain));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 62 (Amarela): Update com Aplicativo Totalmente Nulo")
    void updateSemApp() {
        Long id = 1L;
        AuthPerfilModel domain = AuthPerfilModel.builder().id(id).nmPerfil("TESTE").aplicativo(null).build();

        when(repository.findByNmPerfil(any())).thenReturn(Optional.empty());
        when(repository.update(eq(id), any())).thenReturn(domain);
        assertNotNull(service.update(id, domain));
        verify(aplicativosRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Cobre Linhas 85 (Amarela) e 90, 91 (Vermelha): Delete Sucesso")
    void deleteCompleto() {
        Long idValido = 1L;
        assertDoesNotThrow(() -> service.delete(idValido));
        verify(repository).delete(idValido);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linhas 40, 41 (Amarelas) e 43 (Vermelha): App inexistente no Create")
    void createAppInexistenteNoBanco() {
        Long appId = 99L;
        AplicativosModel appMock = AplicativosModel.builder().id(appId).build();
        AuthPerfilModel domain = AuthPerfilModel.builder()
                .nmPerfil("PERFIL_TESTE")
                .aplicativo(appMock)
                .build();
        when(repository.existsByNmPerfil("PERFIL_TESTE")).thenReturn(false);
        when(aplicativosRepository.findById(appId)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(domain));
        assertTrue(ex.getMessage().contains("Aplicativo informado não encontrado"));
        verify(repository, never()).create(any());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 45 (Vermelha): Sucesso total no Create")
    void createSucessoTotal() {
        Long appId = 1L;
        AplicativosModel appNoBanco = AplicativosModel.builder().id(appId).nmAplicativo("App Real").build();
        AuthPerfilModel domain = AuthPerfilModel.builder()
                .nmPerfil("PERFIL_NOVO")
                .aplicativo(AplicativosModel.builder().id(appId).build())
                .build();

        when(repository.existsByNmPerfil("PERFIL_NOVO")).thenReturn(false);
        when(aplicativosRepository.findById(appId)).thenReturn(Optional.of(appNoBanco));
        when(repository.create(any(AuthPerfilModel.class))).thenReturn(domain);
        AuthPerfilModel result = service.create(domain);
        assertNotNull(result);
        assertEquals(appNoBanco.getNmAplicativo(), domain.getAplicativo().getNmAplicativo());
        verify(repository).create(domain);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 62 (Amarela): Update com Aplicativo sem ID")
    void updateAppComIdNulo() {
        Long id = 1L;
        AplicativosModel appSemId = AplicativosModel.builder().id(null).build();
        AuthPerfilModel domain = AuthPerfilModel.builder().id(id).nmPerfil("TESTE").aplicativo(appSemId).build();

        when(repository.findByNmPerfil(any())).thenReturn(Optional.empty());
        when(repository.update(eq(id), any())).thenReturn(domain);
        service.update(id, domain);
        verify(aplicativosRepository, never()).findById(any());
    }

}
