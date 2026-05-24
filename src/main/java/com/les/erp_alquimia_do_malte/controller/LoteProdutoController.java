package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.response.LoteProdutoResponse;
import com.les.erp_alquimia_do_malte.service.LoteProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lotes-produto")
@Tag(name = "Lotes de Produto (Estoque)")
public class LoteProdutoController {

    private final LoteProdutoService loteProdutoService;

    public LoteProdutoController(LoteProdutoService loteProdutoService) {
        this.loteProdutoService = loteProdutoService;
    }

    @GetMapping
    @Operation(summary = "Listar lotes de produto finalizados em estoque")
    public ResponseEntity<List<LoteProdutoResponse>> listar() {
        return ResponseEntity.ok(loteProdutoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lote de produto por ID")
    public ResponseEntity<LoteProdutoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(loteProdutoService.buscarPorId(id));
    }

    @GetMapping("/proximos-vencimento")
    @Operation(summary = "Listar lotes de produto próximos ao vencimento")
    public ResponseEntity<List<LoteProdutoResponse>> proximosVencimento(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(loteProdutoService.listarProximosVencimento(dias));
    }
}
