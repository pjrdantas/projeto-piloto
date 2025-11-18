package br.com.projeto.piloto.domain.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String nome;
    private String login;
    private String senha;
    private boolean ativo;
    private Set<Role> perfis;
}
