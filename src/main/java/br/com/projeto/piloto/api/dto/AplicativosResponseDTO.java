package br.com.projeto.piloto.api.dto;

import java.time.LocalDateTime;

public record AplicativosResponseDTO(
	    Long id,
	    String nome,
	    String descricao,
	    String url,	    
	    String moduleName,
	    String exposedModule,
	    String routePath,
	    String ativo,
	    LocalDateTime criadoEm,
	    LocalDateTime atualizadoEm
	) {}
