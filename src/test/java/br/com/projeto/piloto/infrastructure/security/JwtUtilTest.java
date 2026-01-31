package br.com.projeto.piloto.infrastructure.security;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.projeto.piloto.infrastructure.config.AuthProperties;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private AuthProperties authProperties;

    @BeforeEach
    void setUp() {

        authProperties = new AuthProperties();
        authProperties.getJwt().setSecret("minha-chave-secreta-muito-longa-e-segura-12345");
        authProperties.getJwt().setExpirationMs(3600000L); // 1 hora
        authProperties.getJwt().setRefreshExpirationMs(7200000L); // 2 horas

        jwtUtil = new JwtUtil(authProperties);
    }

    @Test
    @DisplayName("Deve gerar e extrair dados de um token válido")
    void deveGerarEExtrairDados() {
        String username = "usuario.teste";
        Set<String> authorities = Set.of("READ", "CREATE", "UPDATE", "DELETE");

        String token = jwtUtil.generateToken(username, authorities);

        assertNotNull(token);
        assertTrue(jwtUtil.validate(token));
        assertEquals(username, jwtUtil.getUsername(token));
        
        List<String> extractedAuthorities = jwtUtil.getAuthorities(token);
        assertTrue(extractedAuthorities.containsAll(authorities));
    }

    @Test
    @DisplayName("Deve gerar e validar Refresh Token")
    void deveGerarEValidarRefreshToken() {
        String username = "usuario.refresh";
        String refreshToken = jwtUtil.generateRefreshToken(username);

        assertNotNull(refreshToken);
        assertEquals(username, jwtUtil.extractUsernameFromRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("Deve retornar falso para token expirado ou inválido")
    void deveRetornarFalsoParaTokensInvalidos() {
        assertFalse(jwtUtil.validate("token-totalmente-errado"));

        AuthProperties propsExpiradas = new AuthProperties();
        propsExpiradas.getJwt().setSecret("minha-chave-secreta-muito-longa-e-segura-12345");
        propsExpiradas.getJwt().setExpirationMs(-1000L);
        propsExpiradas.getJwt().setRefreshExpirationMs(-1000L);
        
        JwtUtil jwtExpiradoUtil = new JwtUtil(propsExpiradas);
        String tokenVencido = jwtExpiradoUtil.generateToken("user", Set.of("ADMIN"));
        
        assertFalse(jwtUtil.validate(tokenVencido));
    }

    @Test
    @DisplayName("Deve tratar roles que não são lista ou inexistentes")
    void deveTratarRolesInexistentes() {
        String tokenSemRoles = jwtUtil.generateRefreshToken("user");

        List<String> roles = jwtUtil.getRoles(tokenSemRoles);
        
        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }
}