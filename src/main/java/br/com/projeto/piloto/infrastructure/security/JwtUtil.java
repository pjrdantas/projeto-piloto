package br.com.projeto.piloto.infrastructure.security;

import br.com.projeto.piloto.infrastructure.config.AuthProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtUtil {

    private final Key key;
    private final AuthProperties authProperties;

    // Injeção limpa via construtor usando sua classe de configuração
    public JwtUtil(AuthProperties authProperties) {
        this.authProperties = authProperties;
        // Transforma a String do secret em uma Key segura
        this.key = Keys.hmacShaKeyFor(authProperties.getJwt().getSecret().getBytes());
    }

    public String generateToken(String username, Set<String> roles) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream().toList())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + authProperties.getJwt().getExpirationMs())) // Busca da classe
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + authProperties.getJwt().getRefreshExpirationMs())) // Busca da classe
                .signWith(key)
                .compact();
    }

    // ... (restante dos métodos extractUsername, validate, getUsername, getRoles permanecem iguais)
    
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            // Engloba ExpiredJwtException e outras falhas de assinatura
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream().map(Object::toString).toList();
        }
        return List.of();
    }
    
    public String extractUsernameFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
}