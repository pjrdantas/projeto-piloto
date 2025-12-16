package br.com.projeto.piloto.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record AuthUsuarioResponseDTO(
    Long id,
    String login,
    String nome,
    String ativo,      
    String email,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    Set<AuthPerfilResumoDTO> perfisIds
) {}
