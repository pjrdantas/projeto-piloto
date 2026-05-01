package br.com.projeto.piloto.aplicativos.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
		@NotBlank(message = "Login é obrigatório") String login,
        @NotBlank(message = "Senha é obrigatória") String senha
) {}