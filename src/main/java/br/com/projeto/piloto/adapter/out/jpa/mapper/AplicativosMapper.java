package br.com.projeto.piloto.adapter.out.jpa.mapper;

import java.time.LocalDateTime;

import br.com.projeto.piloto.api.dto.AplicativosRequestDTO;
import br.com.projeto.piloto.api.dto.AplicativosResponseDTO;
import br.com.projeto.piloto.domain.model.AplicativosModel;

public class AplicativosMapper {

    public static AplicativosModel toDomain(AplicativosRequestDTO dto) {
    	 if (dto == null) return null;
        return AplicativosModel.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .url(dto.url())
                .moduleName(dto.moduleName())
                .exposedModule(dto.exposedModule())
                .routePath(dto.routePath())                
                .ativo("S".equalsIgnoreCase(dto.ativo()) ? "S" : "N")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();
    }

    
    public static AplicativosResponseDTO toResponse(AplicativosModel domain) {
    	if (domain == null) return null;
    	
        return new AplicativosResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getUrl(),
                domain.getModuleName(),
                domain.getExposedModule(),
                domain.getRoutePath(),
                domain.getAtivo(), 
                domain.getCriadoEm(),
                domain.getAtualizadoEm()
        );
    }
}
 