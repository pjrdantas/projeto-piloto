package br.com.projeto.piloto.domain.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthPerfilModel {
    private Long id;
    private String nmPerfil;
    private AplicativosModel aplicativo;
    
    @Builder.Default    
    private Set<AuthPermissaoModel> permissoes = Set.of();
}
