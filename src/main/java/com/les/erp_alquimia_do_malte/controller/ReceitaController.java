package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.ReceitaRequest;
import com.les.erp_alquimia_do_malte.dto.response.ReceitaResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.ReceitaService;
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
@RequestMapping("/api/receitas")
@Tag(name = "Receitas (Produção)")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @GetMapping
    @Operation(summary = "Listar receitas")
    public ResponseEntity<List<ReceitaResponse>> listar() {
        return ResponseEntity.ok(receitaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar receita por ID")
    public ResponseEntity<ReceitaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(receitaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Criar receita")
    public ResponseEntity<ReceitaResponse> criar(@Valid @RequestBody ReceitaRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar receita")
    public ResponseEntity<ReceitaResponse> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody ReceitaRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(receitaService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Excluir receita")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        receitaService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
