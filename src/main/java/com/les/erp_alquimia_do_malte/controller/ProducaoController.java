package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.ProducaoRequest;
import com.les.erp_alquimia_do_malte.dto.response.ProducaoResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.ProducaoService;
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
@RequestMapping("/api/producoes")
@Tag(name = "Produção")
public class ProducaoController {

    private final ProducaoService producaoService;

    public ProducaoController(ProducaoService producaoService) {
        this.producaoService = producaoService;
    }

    @GetMapping
    @Operation(summary = "Listar produções")
    public ResponseEntity<List<ProducaoResponse>> listar() {
        return ResponseEntity.ok(producaoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produção por ID")
    public ResponseEntity<ProducaoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(producaoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Registrar produção (consome insumos FIFO, gera lote de produto)")
    public ResponseEntity<ProducaoResponse> registrar(@Valid @RequestBody ProducaoRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producaoService.registrar(request, user.getUsuarioId()));
    }
}
