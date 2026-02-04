package br.com.projeto.piloto.adapter.in.web.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.projeto.piloto.adapter.in.web.dto.AuthPerfilRequestDTO;
import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.model.AuthPermissaoModel;
import br.com.projeto.piloto.domain.port.inbound.AplicativosUseCase;
import br.com.projeto.piloto.domain.port.inbound.AuthPerfilUseCase;
import br.com.projeto.piloto.domain.port.inbound.AuthPermissaoUseCase;

@WebMvcTest(AuthPerfilController.class)
@ContextConfiguration(classes = AuthPerfilController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthPerfilControllerTest {

    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal")
	@MockBean private AuthPerfilUseCase authPerfilUseCase;
    @SuppressWarnings("removal")
	@MockBean private AuthPermissaoUseCase authPermissaoUseCase;
    @SuppressWarnings("removal")
	@MockBean private AplicativosUseCase aplicativosUseCase;
    @Autowired private ObjectMapper objectMapper;

    private AuthPerfilRequestDTO validDto;
    private AuthPerfilModel validModel;

    @BeforeEach
    void setup() {
        validDto = new AuthPerfilRequestDTO("ADMIN", Set.of(1L));
        
        validModel = AuthPerfilModel.builder()
                .id(1L)
                .nmPerfil("ADMIN")
                .build();
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre orElseThrow de Aplicativo e Permissão (Caminhos Vermelhos)")
    void testIllegalArgumentExceptions() {
        when(authPerfilUseCase.existsByNmPerfil(any())).thenReturn(false);
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> 
            mockMvc.perform(post("/api/perfis").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPermissaoUseCase.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> 
            mockMvc.perform(post("/api/perfis").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre ramificação amarela das permissões nulas")
    void testNullPermissionsBranch() throws Exception {
        AuthPerfilRequestDTO dtoNull = new AuthPerfilRequestDTO("ADMIN", null);
        
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPerfilUseCase.create(any())).thenReturn(validModel);

        mockMvc.perform(post("/api/perfis").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoNull)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cobre buildErrorResponse (Outras linhas vermelhas)")
    void testErrorResponses() throws Exception {
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/perfis/1")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/perfis/1")).andExpect(status().isNotFound());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Caminhos de Sucesso")
    void testSuccessPaths() throws Exception {
        when(authPerfilUseCase.existsByNmPerfil(any())).thenReturn(false);
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPermissaoUseCase.findById(anyLong())).thenReturn(Optional.of(new AuthPermissaoModel()));
        when(authPerfilUseCase.create(any())).thenReturn(validModel);

        mockMvc.perform(post("/api/perfis").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists()); 
    }
    
    @Test
    @DisplayName("GET /perfis - Cobertura ListAll")
    void testListAllCoverage() throws Exception {
        when(authPerfilUseCase.listAll()).thenReturn(List.of(validModel));
        mockMvc.perform(get("/api/perfis")).andExpect(status().isOk());
        when(authPerfilUseCase.listAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/perfis")).andExpect(status().isNotFound());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("PUT /perfis/{id} - Cobertura Update")
    void testUpdateCoverage() throws Exception {
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> 
            mockMvc.perform(put("/api/perfis/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPermissaoUseCase.findById(anyLong())).thenReturn(Optional.of(new AuthPermissaoModel()));
        when(authPerfilUseCase.update(anyLong(), any())).thenReturn(validModel);
        
        mockMvc.perform(put("/api/perfis/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("POST /perfis - Cobertura Conflict")
    void testCreateConflict() throws Exception {
        // Cobre a linha: return buildErrorResponse(HttpStatus.CONFLICT...)
        when(authPerfilUseCase.existsByNmPerfil(any())).thenReturn(true);
        mockMvc.perform(post("/api/perfis").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("DELETE /perfis/{id} - Cobertura Success")
    void testDeleteSuccess() throws Exception {
        // Cobre a linha: if (existing.isPresent()) { authPerfilUseCase.delete(id); ...
        when(authPerfilUseCase.findById(1L)).thenReturn(Optional.of(validModel));
        mockMvc.perform(delete("/api/perfis/1")).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /perfis/{id} - Cobertura Success")
    void testFindByIdSuccess() throws Exception {
        // Cobre a linha: if (existing.isPresent()) { AuthPerfilResponseDTO dto = ...
        when(authPerfilUseCase.findById(1L)).thenReturn(Optional.of(validModel));
        mockMvc.perform(get("/api/perfis/1")).andExpect(status().isOk());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Amarelo do Aplicativo não encontrado no Update")
    void testUpdateAppNotFound() {
        // Perfil existe, mas Aplicativo NÃO
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> 
            mockMvc.perform(put("/api/perfis/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Amarelo da Permissão não encontrada no Update")
    void testUpdatePermissionNotFound() {
        // Perfil existe, Aplicativo existe, mas Permissão NÃO
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPermissaoUseCase.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> 
            mockMvc.perform(put("/api/perfis/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Sucesso do Update (Completa a cobertura das branches)")
    void testUpdateSuccessCoverage() throws Exception {
        // Tudo existe (Garante que o orElseThrow não seja disparado)
        when(authPerfilUseCase.findById(anyLong())).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.findById(anyLong())).thenReturn(Optional.of(new AplicativosModel()));
        when(authPermissaoUseCase.findById(anyLong())).thenReturn(Optional.of(new AuthPermissaoModel()));
        when(authPerfilUseCase.update(anyLong(), any())).thenReturn(validModel);

        mockMvc.perform(put("/api/perfis/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
    }

}
