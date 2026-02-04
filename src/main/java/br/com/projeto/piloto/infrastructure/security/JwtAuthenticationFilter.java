package br.com.projeto.piloto.infrastructure.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.projeto.piloto.application.service.AuthSessaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthSessaoService authSessaoService; // 1. ADICIONE O SERVICE

    // 2. ATUALIZE O CONSTRUTOR
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds, AuthSessaoService authSessaoService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
        this.authSessaoService = authSessaoService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasLength(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                // 3. ADICIONE A VALIDAÇÃO DA SESSÃO NO BANCO
                // Além de validar a assinatura do JWT, verificamos se ele consta como "Ativo" no DB
                if (jwtUtil.validate(token) && authSessaoService.validarSessao(token)) {
                    String username = jwtUtil.getUsername(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    // Se o token não estiver na tabela AuthSessao como ativo, limpamos o contexto
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}