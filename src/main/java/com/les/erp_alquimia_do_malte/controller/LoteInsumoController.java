package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.LoteInsumoRequest;
import com.les.erp_alquimia_do_malte.dto.response.LoteInsumoResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.LoteInsumoService;
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
@RequestMapping("/api/lotes-insumo")
@Tag(name = "Lotes de Insumo (Estoque/Logística)")
public class LoteInsumoController {

    private final LoteInsumoService loteInsumoService;

    public LoteInsumoController(LoteInsumoService loteInsumoService) {
        this.loteInsumoService = loteInsumoService;
    }

    @GetMapping
    @Operation(summary = "Listar lotes de insumo em estoque")
    public ResponseEntity<List<LoteInsumoResponse>> listar() {
        return ResponseEntity.ok(loteInsumoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lote de insumo por ID")
    public ResponseEntity<LoteInsumoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(loteInsumoService.buscarPorId(id));
    }

    @GetMapping("/proximos-vencimento")
    @Operation(summary = "Listar lotes de insumo próximos ao vencimento (padrão 30 dias)")
    public ResponseEntity<List<LoteInsumoResponse>> proximosVencimento(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(loteInsumoService.listarProximosVencimento(dias));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Registrar entrada de lote de insumo")
    public ResponseEntity<LoteInsumoResponse> registrarEntrada(@Valid @RequestBody LoteInsumoRequest request,
                                                                @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loteInsumoService.registrarEntrada(request, user.getUsuarioId()));
    }
}
