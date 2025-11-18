package br.com.projeto.piloto.api.dto;

public record AuthResponse(
		String token,
		String refreshToken,
		 String usuario) {
}
