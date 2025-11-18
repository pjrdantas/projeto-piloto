package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record CriarPerfilRequest(String nome, Set<Long> permissoes) {}
