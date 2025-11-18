package br.com.projeto.piloto.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.projeto.piloto.api.dto.UsuarioDTO;
import br.com.projeto.piloto.api.dto.UserRequestDTO;
import br.com.projeto.piloto.application.usecase.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = 
                    "{ \"id\": 1, \"nome\": \"Paulo\", \"login\": \"paulo\", \"ativo\": true, \"perfis\": [\"ADMIN\"] }"
                ))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<?> create(@RequestBody UserRequestDTO request) {

        UsuarioDTO dto = new UsuarioDTO(
                null,
                request.nome(),
                request.login(),
                request.ativo(),
                request.perfisIds()
        );

        UsuarioDTO created = userService.createUser(dto, request.senha());

        return ResponseEntity
                .created(URI.create("/api/usuarios/" + created.id()))
                .body(created);
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um usuário existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = 
                    "{ \"id\": 1, \"nome\": \"Paulo Atualizado\", \"login\": \"paulo\", \"ativo\": true, \"perfis\": [\"ADMIN\"] }"
                ))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request) {

        UsuarioDTO dto = new UsuarioDTO(
                id,
                request.nome(),
                request.login(),
                request.ativo(),
                request.perfisIds()
        );

        return userService.update(id, dto, request.senha())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um usuário pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        boolean deleted = userService.delete(id);

        if (deleted) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}
