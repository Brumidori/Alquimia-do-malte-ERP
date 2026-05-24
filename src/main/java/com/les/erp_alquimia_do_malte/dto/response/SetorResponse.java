package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Setor;

import java.time.LocalDateTime;
import java.util.UUID;

public record SetorResponse(UUID id, String nome, LocalDateTime createdAt) {
    public static SetorResponse from(Setor s) {
        return new SetorResponse(s.getId(), s.getNome(), s.getCreatedAt());
    }
}
