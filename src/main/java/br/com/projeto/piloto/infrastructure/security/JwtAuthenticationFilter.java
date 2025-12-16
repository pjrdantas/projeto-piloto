package br.com.projeto.piloto.infrastructure.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = uds;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();

		if (isPublicPath(path)) {
			filterChain.doFilter(request, response);
			return;
		}

		String header = request.getHeader("Authorization");

		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {

			String token = header.substring(7);

			if (jwtUtil.validate(token)) {

				String username = jwtUtil.getUsername(token);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isPublicPath(String path) {
		return path.startsWith("/api/auth") || path.startsWith("/swagger-ui") || path.equals("/swagger-ui.html")
				|| path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")
				|| path.startsWith("/webjars");
	}
}
