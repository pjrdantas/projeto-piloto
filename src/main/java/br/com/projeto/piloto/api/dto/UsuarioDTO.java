package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record UsuarioDTO(Long id, String nome, String login, Boolean ativo, Set<String> perfis) {}
