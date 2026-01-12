package br.com.projeto.piloto.infrastructure.security;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private UserDetailsService userDetailsService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre isPublicPath: Deve permitir acesso a rotas públicas sem token")
    void devePermitirRotaPublica() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 45-48: Deve retornar 401 se header Authorization estiver ausente ou inválido")
    void deveRetornar401SeHeaderInvalido() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protegida");
        when(request.getHeader("Authorization")).thenReturn(null); // Ou "Basic 123"

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(filterChain);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Linha 54-57: Deve retornar 401 se o token for inválido no JwtUtil")
    void deveRetornar401SeTokenInvalido() throws ServletException, IOException {
        String token = "token.invalido";
        when(request.getRequestURI()).thenReturn("/api/protegida");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validate(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Sucesso: Deve autenticar usuário quando token é válido")
    void deveAutenticarComSucesso() throws ServletException, IOException {
        String token = "token.valido";
        String username = "admin";
        UserDetails userDetails = new User(username, "", Collections.emptyList());

        when(request.getRequestURI()).thenReturn("/api/perfil");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validate(token)).thenReturn(true);
        when(jwtUtil.getUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre Bloco Catch (Linha 75): Deve retornar 401 em caso de qualquer exceção")
    void deveRetornar401EmCasoDeExcecao() throws ServletException, IOException {
        String token = "token.error";
        when(request.getRequestURI()).thenReturn("/api/protegida");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validate(token)).thenThrow(new RuntimeException("Erro genérico"));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre todos os paths do isPublicPath")
    void deveCobrirTodasRotasPublicas() throws ServletException, IOException {
        String[] publicPaths = {
            "/api/auth/register", "/swagger-ui/index.html", "/swagger-ui.html", 
            "/v3/api-docs", "/swagger-resources/conf", "/webjars/js"
        };

        for (String path : publicPaths) {
            when(request.getRequestURI()).thenReturn(path);
            filter.doFilterInternal(request, response, filterChain);
        }
        
        verify(filterChain, times(publicPaths.length)).doFilter(request, response);
    }
}
