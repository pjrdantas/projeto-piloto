package br.com.projeto.piloto.adapter.in.web.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.piloto.api.dto.RegisterRequest;
import br.com.projeto.piloto.domain.exception.DomainException;
import br.com.projeto.piloto.domain.exception.UserNotFoundException;

import jakarta.validation.Valid;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test")
    public static class TestController {

        @GetMapping("/domain-exception")
        public void throwDomainException() {
            throw new DomainException("Erro de negócio teste");
        }

        @GetMapping("/user-not-found")
        public void throwUserNotFoundException() {
            throw new UserNotFoundException("Usuário não encontrado no sistema");
        }

        @GetMapping("/generic-exception")
        public void throwGenericException() {
            throw new RuntimeException("Erro genérico inesperado");
        }

         
        @PostMapping("/validation")
        public void validationTest(@RequestBody @Valid RegisterRequest request) {
           
        }
    }

    @Test
    void deveHandleDomainException() throws Exception {
        mockMvc.perform(get("/test/domain-exception"))
                .andExpect(status().isForbidden())  
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Erro de negócio"))
                .andExpect(jsonPath("$.message").value("Erro de negócio teste"))
                .andExpect(jsonPath("$.path").value("/test/domain-exception"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveHandleUserNotFoundException() throws Exception {
        mockMvc.perform(get("/test/user-not-found"))
                .andExpect(status().isNotFound())  
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Usuário não encontrado"))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado no sistema"))
                .andExpect(jsonPath("$.path").value("/test/user-not-found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveHandleGenericException() throws Exception {
        mockMvc.perform(get("/test/generic-exception"))
                .andExpect(status().isInternalServerError())  
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Erro interno"))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro inesperado no servidor"))
                .andExpect(jsonPath("$.path").value("/test/generic-exception"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveHandleValidationException() throws Exception {
        String jsonInvalido = "{}";  

        mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isUnprocessableEntity())  
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.message").value("Dados enviados são inválidos"))
                .andExpect(jsonPath("$.path").value("/test/validation"))
                .andExpect(jsonPath("$.validationErrors.nome").exists())
                .andExpect(jsonPath("$.validationErrors.login").exists())
                .andExpect(jsonPath("$.validationErrors.senha").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
