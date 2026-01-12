package br.com.projeto.piloto.adapter.in.web.controller;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.com.projeto.piloto.adapter.in.web.dto.LoginRequestDTO;
import br.com.projeto.piloto.adapter.in.web.dto.RefreshTokenRequestDTO;
import br.com.projeto.piloto.application.usecase.AuthInteractor;
import br.com.projeto.piloto.domain.model.AuthPerfilModel; 
import br.com.projeto.piloto.domain.model.AuthUsuarioModel;
import br.com.projeto.piloto.infrastructure.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private AuthInteractor authInteractor;

    @SuppressWarnings("removal")
	@MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthUsuarioModel mockUsuario;

    @BeforeEach
    void setup() {

        AuthPerfilModel perfil = new AuthPerfilModel();
        perfil.setNmPerfil("ROLE_ADMIN");

        mockUsuario = new AuthUsuarioModel();
        mockUsuario.setLogin("admin");
        mockUsuario.setNome("Administrador");
        mockUsuario.setPerfis(Set.of(perfil));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Login - Sucesso")
    void loginSuccess() throws Exception {
        LoginRequestDTO loginDto = new LoginRequestDTO("admin", "123");

        when(authInteractor.authenticate(anyString(), anyString())).thenReturn(mockUsuario);
        when(jwtUtil.generateToken(anyString(), anySet())).thenReturn("token-access");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("token-refresh");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-access"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Sucesso")
    void refreshSuccess() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("valid-refresh");

        when(jwtUtil.validate("valid-refresh")).thenReturn(true);
        when(jwtUtil.extractUsernameFromRefreshToken("valid-refresh")).thenReturn("admin");
        when(authInteractor.findByLogin("admin")).thenReturn(mockUsuario);
        when(jwtUtil.generateToken(anyString(), anySet())).thenReturn("new-access-token");

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Token Nulo ou Vazio (400)")
    void refreshBadRequest() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("");

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O token de refresh não pode ser nulo ou vazio"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Token Inválido (401)")
    void refreshInvalid() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("invalid-token");
        when(jwtUtil.validate(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Refresh token inválido ou expirado"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Catch ExpiredJwtException (401)")
    void refreshExpired() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("expired");
        when(jwtUtil.validate(anyString())).thenThrow(new ExpiredJwtException(null, null, "expired"));

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("O token de refresh expirou"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Catch MalformedJwtException (400)")
    void refreshMalformed() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("malformed");
        when(jwtUtil.validate(anyString())).thenThrow(new MalformedJwtException("malformed"));

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O token de refresh fornecido é inválido ou malformado"));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Refresh - Catch JwtException (500)")
    void refreshJwtException() throws Exception {
        RefreshTokenRequestDTO refreshDto = new RefreshTokenRequestDTO("error");
        when(jwtUtil.validate(anyString())).thenThrow(new JwtException("Generic Error"));

        mockMvc.perform(post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }
}
