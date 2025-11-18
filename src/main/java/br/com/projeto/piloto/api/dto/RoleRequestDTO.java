package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record RoleRequestDTO(
    String nome,
    Set<Long> permissoesIds
) {}
