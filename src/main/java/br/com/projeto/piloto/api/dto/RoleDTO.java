package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record RoleDTO(
		Long id, 
		String nome, 
		Set<String> permissoes
		) {}
