package br.com.projeto.piloto.adapter.in.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.projeto.piloto.api.dto.PermissionDTO;
import br.com.projeto.piloto.application.usecase.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Cria uma nova permissão")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Permissão criada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = 
                    "{ \"id\": 1, \"nome\": \"READ\" }"
                ))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = br.com.projeto.piloto.adapter.in.web.exception.ErrorResponse.class)))
    })
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO request) {
        PermissionDTO created = permissionService.create(request);
        return ResponseEntity
                .created(URI.create("/api/permissions/" + created.id()))
                .body(created);
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Lista todas as permissões")
    public ResponseEntity<List<PermissionDTO>> findAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Busca uma permissão pelo ID")
    public ResponseEntity<PermissionDTO> findById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de uma permissão existente")
    public ResponseEntity<PermissionDTO> update(
            @PathVariable Long id,
            @RequestBody PermissionDTO request) {

        return permissionService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma permissão pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = permissionService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
