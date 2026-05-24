package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.ProdutoRequest;
import com.les.erp_alquimia_do_malte.dto.response.ProdutoResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.ProdutoService;
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
@RequestMapping("/api/produtos")
@Tag(name = "Produtos (Produção)")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(summary = "Listar produtos cadastrados")
    public ResponseEntity<List<ProdutoResponse>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Criar produto")
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody ProdutoRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(produtoService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Excluir produto")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        produtoService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
