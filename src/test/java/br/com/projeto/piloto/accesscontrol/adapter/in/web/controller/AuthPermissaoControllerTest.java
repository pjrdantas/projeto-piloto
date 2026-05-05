
package br.com.projeto.piloto.accesscontrol.adapter.in.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

import br.com.projeto.piloto.accesscontrol.adapter.in.web.dto.AuthPermissaoRequestDTO;
import br.com.projeto.piloto.accesscontrol.application.port.in.AuthPermissaoUseCasePort;
import br.com.projeto.piloto.accesscontrol.domain.model.AuthPermissaoModel;

@WebMvcTest(AuthPermissaoController.class)
@ContextConfiguration(classes = AuthPermissaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthPermissaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private AuthPermissaoUseCasePort authPermissaoUseCasePort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AuthPermissaoRequestDTO validDto;
    private AuthPermissaoModel validModel;

    @BeforeEach
    void setup() {
        validDto = new AuthPermissaoRequestDTO("READ_PRIVILEGE");
        validModel = new AuthPermissaoModel();
        validModel.setId(1L);
        validModel.setNmPermissao("READ_PRIVILEGE");
    }

    @Test
    @DisplayName("Caminhos de Sucesso e Conflito (POST, GET ID, DELETE, PUT)")
    void testFullWorkflow() throws Exception {
        when(authPermissaoUseCasePort.existsByNmPermissao(any())).thenReturn(false);
        when(authPermissaoUseCasePort.create(any())).thenReturn(validModel);
        mockMvc.perform(post("/api/permissoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated());
        when(authPermissaoUseCasePort.existsByNmPermissao(any())).thenReturn(true);
        mockMvc.perform(post("/api/permissoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isConflict());
        when(authPermissaoUseCasePort.findById(1L)).thenReturn(Optional.of(validModel));
        mockMvc.perform(get("/api/permissoes/1")).andExpect(status().isOk());
        when(authPermissaoUseCasePort.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/permissoes/99")).andExpect(status().isNotFound());
        when(authPermissaoUseCasePort.findById(1L)).thenReturn(Optional.of(validModel));
        when(authPermissaoUseCasePort.update(eq(1L), any())).thenReturn(validModel);
        mockMvc.perform(put("/api/permissoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
        when(authPermissaoUseCasePort.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/permissoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isNotFound());
        when(authPermissaoUseCasePort.findById(1L)).thenReturn(Optional.of(validModel));
        doNothing().when(authPermissaoUseCasePort).delete(1L);
        mockMvc.perform(delete("/api/permissoes/1")).andExpect(status().isNoContent());
        when(authPermissaoUseCasePort.findById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/permissoes/2")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("LIST ALL - Cobertura da lista populada e lista vazia")
    void testListAllCoverage() throws Exception {
        when(authPermissaoUseCasePort.listAll()).thenReturn(List.of(validModel));
        mockMvc.perform(get("/api/permissoes")).andExpect(status().isOk());
        when(authPermissaoUseCasePort.listAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/permissoes")).andExpect(status().isNotFound());
    }
}
