package br.com.projeto.piloto.infrastructure.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.projeto.piloto.infrastructure.config.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final AuthProperties authProperties;

    
    public JwtUtil(AuthProperties authProperties) {
        this.authProperties = authProperties;
        
        this.key = Keys.hmacShaKeyFor(authProperties.getJwt().getSecret().getBytes());
    }

    public String generateToken(String username, Set<String> authorities) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                // Agora enviamos a lista completa para o Angular
                .claim("authorities", authorities.stream().toList()) 
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + authProperties.getJwt().getExpirationMs())) 
                .signWith(key)
                .compact();
    }
    
    public List<String> getAuthorities(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object auths = claims.get("authorities");
        if (auths instanceof List<?>) {
            return ((List<?>) auths).stream().map(Object::toString).toList();
        }
        return List.of();
    }    

    public String generateRefreshToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + authProperties.getJwt().getRefreshExpirationMs())) 
                .signWith(key)
                .compact();
    }

    
    
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            
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

    /**
     * Extrai a data de expiração do token em LocalDateTime
     */
    public LocalDateTime extractExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        
        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}