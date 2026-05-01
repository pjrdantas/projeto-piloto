package br.com.projeto.piloto.aplicativos.adapter.in.web.dto;

import java.util.Set;

public record AuthPerfilResponseDTO(
        Long id,
        String nmPerfil,
        Set<AuthPermissaoResponseDTO> permissoes 
) {}
