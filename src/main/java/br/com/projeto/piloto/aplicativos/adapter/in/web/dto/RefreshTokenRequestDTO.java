package br.com.projeto.piloto.aplicativos.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
    @NotBlank(message = "refreshToken é obrigatório") String refreshToken
) {}
