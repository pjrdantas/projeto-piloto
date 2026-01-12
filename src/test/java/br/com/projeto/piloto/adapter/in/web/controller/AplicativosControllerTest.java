package br.com.projeto.piloto.adapter.in.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import br.com.projeto.piloto.adapter.in.web.dto.AplicativosRequestDTO;
import br.com.projeto.piloto.domain.model.AplicativosModel;
import br.com.projeto.piloto.domain.port.inbound.AplicativosUseCase;

@WebMvcTest(AplicativosController.class)
@ContextConfiguration(classes = AplicativosController.class) 
@AutoConfigureMockMvc(addFilters = false)
class AplicativosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private AplicativosUseCase aplicativosUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private AplicativosRequestDTO validDto;
    private AplicativosModel validModel;

    @BeforeEach
    void setup() {

        validDto = new AplicativosRequestDTO("App", "Desc", "http://url", "mod", "exp", "/path", "S");

        validModel = AplicativosModel.builder()
                .id(1L)
                .nmAplicativo("App")
                .dsAplicativo("Desc")
                .dsUrl("http://url")
                .flAtivo("S")
                .build();
    }

    @SuppressWarnings("null")
	@Test
    void testFullCoverage() throws Exception {

        when(aplicativosUseCase.existsByNome(any())).thenReturn(false);
        when(aplicativosUseCase.create(any())).thenReturn(validModel);
        mockMvc.perform(post("/api/aplicativos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isCreated());

        when(aplicativosUseCase.existsByNome(any())).thenReturn(true);
        mockMvc.perform(post("/api/aplicativos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isConflict());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.of(validModel));
        mockMvc.perform(get("/api/aplicativos/1")).andExpect(status().isOk());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/aplicativos/1")).andExpect(status().isNotFound());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.of(validModel));
        when(aplicativosUseCase.update(eq(1L), any())).thenReturn(validModel);
        mockMvc.perform(put("/api/aplicativos/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isOk());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/aplicativos/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))).andExpect(status().isNotFound());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.of(validModel));
        mockMvc.perform(delete("/api/aplicativos/1")).andExpect(status().isNoContent());

        when(aplicativosUseCase.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/aplicativos/1")).andExpect(status().isNotFound());

        when(aplicativosUseCase.listAll()).thenReturn(Collections.singletonList(validModel));
        mockMvc.perform(get("/api/aplicativos")).andExpect(status().isOk());
    }
    
    @SuppressWarnings("null")
	@Test
    @DisplayName("GET - Listar todos")
    void listAllSuccess() throws Exception {
        when(aplicativosUseCase.listAll()).thenReturn(List.of(validModel));

        mockMvc.perform(get("/api/aplicativos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists()); 
    }
    
    @Test
    @DisplayName("GET - Cobertura Full List")
    void testListAllFullCoverage() throws Exception {
        when(aplicativosUseCase.listAll()).thenReturn(List.of(validModel));

        mockMvc.perform(get("/api/aplicativos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
    
    @SuppressWarnings("null")
	@Test
    @DisplayName("GET /ativos - Listar ativos")
    void listAtivosSuccess() throws Exception {
        when(aplicativosUseCase.listAll()).thenReturn(List.of(validModel));

        mockMvc.perform(get("/api/aplicativos/ativos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }
}
