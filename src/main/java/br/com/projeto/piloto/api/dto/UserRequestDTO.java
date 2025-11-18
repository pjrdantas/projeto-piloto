package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record UserRequestDTO(
        String nome,
        String login,
        String senha,
        String ativo,
        Set<Long> perfisIds
) {}
