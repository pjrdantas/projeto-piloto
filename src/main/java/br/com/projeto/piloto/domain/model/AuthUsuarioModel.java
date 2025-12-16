package br.com.projeto.piloto.domain.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUsuarioModel {

    private Long id;
    private String login;
    private String senha;
    private String nome;
    private String ativo;  
    private String email;
    
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    private Set<AuthPerfilModel> perfis;

}
