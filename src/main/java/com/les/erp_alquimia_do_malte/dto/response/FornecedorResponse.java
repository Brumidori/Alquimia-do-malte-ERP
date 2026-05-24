package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Fornecedor;

import java.time.LocalDateTime;
import java.util.UUID;

public record FornecedorResponse(UUID id, String nome, String cnpj, String telefone, String email, LocalDateTime createdAt) {
    public static FornecedorResponse from(Fornecedor f) {
        return new FornecedorResponse(f.getId(), f.getNome(), f.getCnpj(), f.getTelefone(), f.getEmail(), f.getCreatedAt());
    }
}
