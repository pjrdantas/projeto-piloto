package br.com.projeto.piloto.api.dto;

public record AuthResponseDTO(
		String token,
		String refreshToken,
		 String usuario) {
}
