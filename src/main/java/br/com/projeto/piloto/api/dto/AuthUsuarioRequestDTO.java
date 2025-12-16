package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record AuthUsuarioRequestDTO(
    String login,
    String senha,
    String nome,
    String ativo,
    String email,
    Set<Long> perfisIds   
) {}
