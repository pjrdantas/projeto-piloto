package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record CriarUsuarioRequest(String nome, String login, String senha, Set<Long> perfis) {}
