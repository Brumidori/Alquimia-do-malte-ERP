package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.VendaRequest;
import com.les.erp_alquimia_do_malte.dto.response.VendaResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.VendaService;
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
@RequestMapping("/api/vendas")
@Tag(name = "Vendas (Comercial)")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping
    @Operation(summary = "Listar vendas")
    public ResponseEntity<List<VendaResponse>> listar() {
        return ResponseEntity.ok(vendaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID")
    public ResponseEntity<VendaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Registrar venda (abate estoque de lotes de produto)")
    public ResponseEntity<VendaResponse> criar(@Valid @RequestBody VendaRequest request,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.criar(request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Cancelar venda (estorna estoque)")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        vendaService.cancelar(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
