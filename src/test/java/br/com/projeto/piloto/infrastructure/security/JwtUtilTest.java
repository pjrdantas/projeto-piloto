package br.com.projeto.piloto.infrastructure.security;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "minha-chave-secreta-muito-longa-e-segura-12345";
    private final long expiration = 3600000; // 1 hora
    private final long refreshExpiration = 7200000; // 2 horas

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, expiration, refreshExpiration);
    }

    @Test
    @DisplayName("Deve gerar e extrair dados de um token válido")
    void deveGerarEExtrairDados() {
        String username = "usuario.teste";
        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_USER");

        String token = jwtUtil.generateToken(username, roles);

        assertNotNull(token);
        assertTrue(jwtUtil.validate(token));
        assertEquals(username, jwtUtil.getUsername(token));
        
        List<String> extractedRoles = jwtUtil.getRoles(token);
        assertTrue(extractedRoles.containsAll(roles));
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
    @DisplayName("Cobre Linha 52 e 54 (Catch): Deve retornar falso para token expirado ou inválido")
    void deveRetornarFalsoParaTokensInvalidos() {
        assertFalse(jwtUtil.validate("token-totalmente-errado"));
        JwtUtil jwtExpiradoUtil = new JwtUtil(secret, -1000, -1000);
        String tokenVencido = jwtExpiradoUtil.generateToken("user", Set.of("ADMIN"));
        
        assertFalse(jwtUtil.validate(tokenVencido));
    }

    @Test
    @DisplayName("Cobre Linha 67 e 70: Deve tratar roles que não são lista ou inexistentes")
    void deveTratarRolesInexistentes() {
        String tokenSemRoles = jwtUtil.generateRefreshToken("user");

        List<String> roles = jwtUtil.getRoles(tokenSemRoles);
        
        assertNotNull(roles);
        assertTrue(roles.isEmpty()); // Cobre a linha 70 (return List.of())
    }

    @Test
    @DisplayName("Cobre Linha 67: Roles como tipo diferente de List")
    void deveValidarRolesInstanciaDiferente() {
        String token = jwtUtil.generateToken("user", Set.of("ADMIN"));
        List<String> roles = jwtUtil.getRoles(token);
        assertFalse(roles.isEmpty());
    }
}
