package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.TipoInsumoRequest;
import com.les.erp_alquimia_do_malte.dto.response.TipoInsumoResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.TipoInsumoService;
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
@RequestMapping("/api/tipos-insumo")
@Tag(name = "Tipos de Insumo (Produção)")
public class TipoInsumoController {

    private final TipoInsumoService tipoInsumoService;

    public TipoInsumoController(TipoInsumoService tipoInsumoService) {
        this.tipoInsumoService = tipoInsumoService;
    }

    @GetMapping
    @Operation(summary = "Listar tipos de insumo")
    public ResponseEntity<List<TipoInsumoResponse>> listar() {
        return ResponseEntity.ok(tipoInsumoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de insumo por ID")
    public ResponseEntity<TipoInsumoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(tipoInsumoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Criar tipo de insumo")
    public ResponseEntity<TipoInsumoResponse> criar(@Valid @RequestBody TipoInsumoRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoInsumoService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar tipo de insumo")
    public ResponseEntity<TipoInsumoResponse> atualizar(@PathVariable UUID id,
                                                         @Valid @RequestBody TipoInsumoRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(tipoInsumoService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Excluir tipo de insumo")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        tipoInsumoService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
