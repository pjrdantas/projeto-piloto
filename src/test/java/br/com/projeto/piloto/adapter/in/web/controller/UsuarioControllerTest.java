package br.com.projeto.piloto.adapter.in.web.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.projeto.piloto.adapter.in.web.dto.AuthUsuarioRequestDTO;
import br.com.projeto.piloto.domain.exception.UserNotFoundException;
import br.com.projeto.piloto.domain.model.AuthPerfilModel;
import br.com.projeto.piloto.domain.model.AuthUsuarioModel;
import br.com.projeto.piloto.domain.port.inbound.AuthUsuarioUseCasePort;
import jakarta.servlet.ServletException;

@WebMvcTest(UsuarioController.class)
@ContextConfiguration(classes = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal")
	@MockBean private AuthUsuarioUseCasePort usuarioUseCase;
    @Autowired private ObjectMapper objectMapper;

    private AuthUsuarioRequestDTO validDto;
    private AuthUsuarioModel validModel;

    @BeforeEach
    void setup() {
        validDto = new AuthUsuarioRequestDTO("admin", "senha123", "Nome", "S", "email@teste.com", Set.of(1L));
        
        AuthPerfilModel perfil = AuthPerfilModel.builder().id(1L).nmPerfil("ADMIN").build();
        validModel = AuthUsuarioModel.builder()
                .id(1L).login("admin").nome("Nome").ativo("S").email("email@teste.com")
                .perfis(Set.of(perfil)).build();
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Sucesso e toDto (Linhas verdes nos métodos principais)")
    void testSuccessFlow() throws Exception {
        when(usuarioUseCase.criar(any())).thenReturn(validModel);
        when(usuarioUseCase.atualizar(any(), any())).thenReturn(validModel);
        doNothing().when(usuarioUseCase).deletar(any());

        mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isCreated());

        mockMvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isOk());

        mockMvc.perform(delete("/api/usuarios/1")).andExpect(status().isNoContent());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre validarEConverterPerfis (Remove Amarelo e Vermelho das validações)")
    void testValidationPerfis() throws Exception {
        AuthUsuarioRequestDTO dtoEmpty = new AuthUsuarioRequestDTO("u", "p", "n", "S", "e", Collections.emptySet());
        assertThrows(ServletException.class, () -> 
            mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoEmpty))));
        Set<Long> idsComNulo = new HashSet<>();
        idsComNulo.add(null);
        AuthUsuarioRequestDTO dtoNullId = new AuthUsuarioRequestDTO("u", "p", "n", "S", "e", idsComNulo);
        assertThrows(ServletException.class, () -> 
            mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoNullId))));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre normalizarAtivo (Amarelo na branch do Ativo)")
    void testNormalizarAtivo() throws Exception {
        AuthUsuarioRequestDTO dtoInvalido = new AuthUsuarioRequestDTO("u", "p", "n", "X", "e", Set.of(1L));
        when(usuarioUseCase.criar(any())).thenReturn(validModel);

        mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido))).andExpect(status().isCreated());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Exception Catches (Vermelho nos blocos Catch)")
    void testExceptionHandlers() throws Exception {
        when(usuarioUseCase.criar(any())).thenThrow(new DataIntegrityViolationException("Conflito"));
        assertThrows(ServletException.class, () -> 
            mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))));
        when(usuarioUseCase.atualizar(any(), any())).thenThrow(new UserNotFoundException("404"));
        assertThrows(ServletException.class, () -> 
            mockMvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))));
    }
    
    @SuppressWarnings("null")
	@Test
    @DisplayName("PUT - Cobre o catch de DataIntegrityViolationException no Update")
    void testUpdateDataIntegrityViolation() throws Exception {
        when(usuarioUseCase.atualizar(anyLong(), any()))
            .thenThrow(new DataIntegrityViolationException("Conflito de login"));

        assertThrows(ServletException.class, () -> 
            mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
        );
    }
    @Test
    @DisplayName("GET - Cobre findById e o método toDto")
    void testFindByIdSuccess() throws Exception {
        when(usuarioUseCase.buscarPorId(1L)).thenReturn(validModel);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("admin"));
    }

    @Test
    @DisplayName("GET - Cobre listAll e o stream com map(this::toDto)")
    void testListAllSuccess() throws Exception {
        when(usuarioUseCase.listarTodos()).thenReturn(List.of(validModel));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value("admin"));
    }

}
