package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.UsuarioRequest;
import com.les.erp_alquimia_do_malte.dto.response.UsuarioResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuários (Admin)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar usuário")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody UsuarioRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request, user.getUsuarioId()));
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativar/Desativar usuário")
    public ResponseEntity<UsuarioResponse> alternarAtivo(@PathVariable UUID id,
                                                          @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(usuarioService.alternarAtivo(id, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
