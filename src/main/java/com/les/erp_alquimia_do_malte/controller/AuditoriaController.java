package com.les.erp_alquimia_do_malte.controller;

import com.les.erp_alquimia_do_malte.dto.response.AuditoriaResponse;
import com.les.erp_alquimia_do_malte.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auditoria")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Auditoria (Admin)")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as ações registradas (paginado)")
    public ResponseEntity<Page<AuditoriaResponse>> listar(
            @PageableDefault(size = 50, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.listar(pageable));
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtrar auditoria por tabela e período")
    public ResponseEntity<Page<AuditoriaResponse>> filtrar(
            @RequestParam String tabela,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.filtrar(tabela, inicio, fim, pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar ações de um usuário específico")
    public ResponseEntity<Page<AuditoriaResponse>> porUsuario(
            @PathVariable UUID usuarioId,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.porUsuario(usuarioId, pageable));
    }
}
