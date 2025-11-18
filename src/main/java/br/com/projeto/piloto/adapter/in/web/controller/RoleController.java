package br.com.projeto.piloto.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.piloto.api.dto.RoleDTO;
import br.com.projeto.piloto.application.usecase.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Cria um novo perfil (role)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = 
                    "{ \"id\": 1, \"nome\": \"ADMIN\", \"permissoes\": [\"READ\", \"WRITE\"] }"
                ))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO request) {
        RoleDTO created = roleService.create(request);
        return ResponseEntity
                .created(URI.create("/api/roles/" + created.id()))
                .body(created);
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Lista todos os perfis")
    public ResponseEntity<List<RoleDTO>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Busca um perfil pelo ID")
    public ResponseEntity<RoleDTO> findById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um perfil existente")
    public ResponseEntity<RoleDTO> update(
            @PathVariable Long id,
            @RequestBody RoleDTO request) {

        return roleService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um perfil pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = roleService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
