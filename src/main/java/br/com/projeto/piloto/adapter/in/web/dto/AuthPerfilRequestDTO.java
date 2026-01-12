package br.com.projeto.piloto.adapter.in.web.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthPerfilRequestDTO(
        @NotBlank(message = "O campo 'nmPerfil' não pode ser nulo ou vazio") String nmPerfil,
        @NotNull Long aplicativoId, 
        Set<Long> permissoesIds
) { }
