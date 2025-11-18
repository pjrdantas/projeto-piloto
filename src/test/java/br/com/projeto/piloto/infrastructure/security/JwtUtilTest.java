package br.com.projeto.piloto.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Key;
import java.util.List;
import java.util.Set;


class JwtUtilTest {

    private final String secret = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    @Test
    void deveGerarEValidarTokenDeAcesso() {
        JwtUtil jwtUtil = new JwtUtil(secret, 60_000L, 120_000L);

        String token = jwtUtil.generateToken("usuario1", Set.of("ROLE_USER", "ROLE_ADMIN"));
        assertNotNull(token);
        assertTrue(jwtUtil.validate(token));
        assertEquals("usuario1", jwtUtil.getUsername(token));

        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        List<?> roles = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);

        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void deveGerarEValidarRefreshTokenEExtrairUsername() {
        JwtUtil jwtUtil = new JwtUtil(secret, 60_000L, 120_000L);

        String refresh = jwtUtil.generateRefreshToken("refreshUser");
        assertNotNull(refresh);
        assertTrue(jwtUtil.validate(refresh));
        assertEquals("refreshUser", jwtUtil.extractUsernameFromRefreshToken(refresh));
    }

    @Test
    void tokenAlteradoDeveSerInvalido() {
         
        JwtUtil jwtUtilValido = new JwtUtil(secret, 60_000L, 120_000L);
        String token = jwtUtilValido.generateToken("usuario", Set.of());
        assertTrue(jwtUtilValido.validate(token));

         
        String outroSecret = "abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789";
        JwtUtil jwtUtilInvalido = new JwtUtil(outroSecret, 60_000L, 120_000L);

         
        assertFalse(jwtUtilInvalido.validate(token));
    }


    @Test
    void tokenExpiradoDeveSerInvalido() throws InterruptedException {
         
        JwtUtil jwtUtil = new JwtUtil(secret, 1L, 120_000L);

        String token = jwtUtil.generateToken("user-exp", Set.of());
        assertNotNull(token);

         
        Thread.sleep(10);

        assertFalse(jwtUtil.validate(token));
    }
}
