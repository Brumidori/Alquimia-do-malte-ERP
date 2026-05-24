package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Cliente;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(UUID id, String nome, String cnpj, String telefone, String email, LocalDateTime createdAt) {
    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), c.getCnpj(), c.getTelefone(), c.getEmail(), c.getCreatedAt());
    }
}
