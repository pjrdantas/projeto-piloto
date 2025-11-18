package br.com.projeto.piloto.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "refreshToken é obrigatório") String refreshToken
) {}
