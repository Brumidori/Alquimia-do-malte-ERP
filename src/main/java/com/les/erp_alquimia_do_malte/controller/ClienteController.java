package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.request.ClienteRequest;
import com.les.erp_alquimia_do_malte.dto.response.ClienteResponse;
import com.les.erp_alquimia_do_malte.security.CustomUserDetails;
import com.les.erp_alquimia_do_malte.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes (Comercial)")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes")
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Criar cliente")
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criar(request, user.getUsuarioId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody ClienteRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(clienteService.atualizar(id, request, user.getUsuarioId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Excluir cliente")
    public ResponseEntity<Void> excluir(@PathVariable UUID id,
                                         @AuthenticationPrincipal CustomUserDetails user) {
        clienteService.excluir(id, user.getUsuarioId());
        return ResponseEntity.noContent().build();
    }
}
