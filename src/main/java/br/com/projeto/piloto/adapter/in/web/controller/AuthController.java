package br.com.projeto.piloto.adapter.in.web.controller;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.projeto.piloto.api.dto.AuthResponseDTO;
import br.com.projeto.piloto.api.dto.LoginRequestDTO;
import br.com.projeto.piloto.api.dto.RefreshTokenRequestDTO;
import br.com.projeto.piloto.api.dto.RegisterRequestDTO;
import br.com.projeto.piloto.application.usecase.AuthService;
import br.com.projeto.piloto.domain.model.User;
import br.com.projeto.piloto.infrastructure.security.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{ \"token\": \"access-token\", \"refreshToken\": \"refresh-token\", \"login\": \"usuario\" }"))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        User user = authService.authenticate(request.login(), request.senha());
        Set<String> roles = user.getPerfis().stream().map(r -> r.getNome()).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(user.getLogin(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getLogin());
        return ResponseEntity.ok(new AuthResponseDTO(token, refreshToken, user.getLogin()));
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de um novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{ \"id\": 1, \"nome\": \"Paulo\", \"login\": \"paulo123\" }"))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        User novoUser = new User();
        novoUser.setNome(request.nome());
        novoUser.setLogin(request.login());
        novoUser.setSenha(request.senha());
        User created = authService.register(novoUser);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Renova o access token usando refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Novo access token gerado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{ \"accessToken\": \"novo-access-token\" }"))),
        @ApiResponse(responseCode = "400", description = "Token inválido ou argumento inválido",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Token expirado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDTO request) {
        String refreshToken = request.refreshToken();
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("O token de refresh não pode ser nulo ou vazio");
        }

        String login;
        try {
            login = jwtUtil.extractUsernameFromRefreshToken(refreshToken);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "O token de refresh expirou");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("O token de refresh fornecido é inválido ou malformado");
        } catch (JwtException ex) {
            throw new JwtException("Erro ao processar o token de refresh: " + ex.getMessage());
        }

        String newAccessToken = jwtUtil.generateToken(login, Set.of());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
