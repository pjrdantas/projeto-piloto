package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record UserResponseDTO(
        Long id,
        String nome,
        String login,
        String ativo,
        Set<RoleDTO> perfis
) {

    public record RoleDTO(Long id, String nome) {}
}
