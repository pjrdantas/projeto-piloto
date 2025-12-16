package br.com.projeto.piloto.domain.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "id", "nome", "descricao", "url", "moduleName", "exposedModule", "routePath", "ativo", "criadoEm", "atualizadoEm" })
public class AplicativosModel {
    private Long id;
    private String nome;
    private String descricao;
    private String url;
    private String moduleName;
    private String exposedModule;
    private String routePath;
    private String ativo;  
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
