package br.com.projeto.piloto.api.dto;

import java.util.Set;

public record AuthResponseDTO(
        String token, 
        String refreshToken, 
        String usuario,
        String nome,          
        Set<String> perfis
) {
}
