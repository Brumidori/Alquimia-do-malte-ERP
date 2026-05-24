package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.FornecedorRequest;
import com.les.erp_alquimia_do_malte.dto.response.FornecedorResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.FornecedorService;
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
@RequestMapping("/api/fornecedores")
@Tag(name = "Fornecedores (Comercial)")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    @Operation(summary = "Listar fornecedores")
    public ResponseEntity<List<FornecedorResponse>> listar() {
        return ResponseEntity.ok(fornecedorService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID")
    public ResponseEntity<FornecedorResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Criar fornecedor")
    public ResponseEntity<FornecedorResponse> criar(@Valid @RequestBody FornecedorRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar fornecedor")
    public ResponseEntity<FornecedorResponse> atualizar(@PathVariable UUID id,
                                                         @Valid @RequestBody FornecedorRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Excluir fornecedor")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        fornecedorService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
