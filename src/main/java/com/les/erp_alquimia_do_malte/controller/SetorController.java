package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.SetorRequest;
import com.les.erp_alquimia_do_malte.dto.response.SetorResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.SetorService;
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
@RequestMapping("/api/setores")
@Tag(name = "Setores")
public class SetorController {

    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @GetMapping
    @Operation(summary = "Listar setores")
    public ResponseEntity<List<SetorResponse>> listar() {
        return ResponseEntity.ok(setorService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar setor por ID")
    public ResponseEntity<SetorResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar setor")
    public ResponseEntity<SetorResponse> criar(@Valid @RequestBody SetorRequest request,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar setor")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable UUID id,
                                                    @Valid @RequestBody SetorRequest request,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(setorService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir setor (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        setorService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
