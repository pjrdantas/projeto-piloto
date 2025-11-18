package br.com.projeto.piloto.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
    @NotBlank(message = "refreshToken é obrigatório") String refreshToken
) {}
