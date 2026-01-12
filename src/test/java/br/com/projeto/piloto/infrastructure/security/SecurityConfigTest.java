package br.com.projeto.piloto.infrastructure.security;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private UserDetailsService userDetailsService;
    @Mock private AuthenticationConfiguration authConfig;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtUtil, userDetailsService);
    }

    @Test
    @DisplayName("Cobre Beans de Auth e Password")
    void deveCobrirBeansSimples() throws Exception {
        assertNotNull(securityConfig.passwordEncoder());
        when(authConfig.getAuthenticationManager()).thenReturn(mock(org.springframework.security.authentication.AuthenticationManager.class));
        assertNotNull(securityConfig.authenticationManager(authConfig));
    }

    @SuppressWarnings("null")
	@Test
    @DisplayName("Cobre CORS - Resolve NPEs e StubbingException")
    void deveCobrirConfiguracaoCorsCompleta() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletMapping mapping = mock(HttpServletMapping.class);
        lenient().when(request.getContextPath()).thenReturn("");
        lenient().when(request.getRequestURI()).thenReturn("/any-path");
        lenient().when(request.getServletPath()).thenReturn("");
        lenient().when(request.getPathInfo()).thenReturn(null);
        lenient().when(request.getHttpServletMapping()).thenReturn(mapping);

        CorsConfiguration config = source.getCorsConfiguration(request);
        
        assertNotNull(config);
        assertEquals(List.of("*"), config.getAllowedOrigins());
        assertEquals(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"), config.getAllowedMethods());
        assertEquals(List.of("*"), config.getAllowedHeaders());
        assertFalse(config.getAllowCredentials());
    }


    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Cobre FilterChain Lambdas - 100% de Cobertura")
    void deveCobrindoFilterChainELambdas() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        when(http.csrf(any(Customizer.class))).thenAnswer(inv -> {
            Customizer<CsrfConfigurer<HttpSecurity>> customizer = inv.getArgument(0);
            customizer.customize(mock(CsrfConfigurer.class));
            return http;
        });

        when(http.cors(any(Customizer.class))).thenAnswer(inv -> {
            Customizer<CorsConfigurer<HttpSecurity>> customizer = inv.getArgument(0);
            customizer.customize(mock(CorsConfigurer.class));
            return http;
        });

        when(http.sessionManagement(any(Customizer.class))).thenAnswer(inv -> {
            Customizer<SessionManagementConfigurer<HttpSecurity>> customizer = inv.getArgument(0);
            customizer.customize(mock(SessionManagementConfigurer.class));
            return http;
        });

        when(http.authorizeHttpRequests(any(Customizer.class))).thenAnswer(inv -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer = inv.getArgument(0);
            var registry = mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class, RETURNS_DEEP_STUBS);
            customizer.customize(registry);
            return http;
        });

        SecurityFilterChain result = securityConfig.filterChain(http);

        assertNotNull(result);
        verify(http).build();
    }
}
