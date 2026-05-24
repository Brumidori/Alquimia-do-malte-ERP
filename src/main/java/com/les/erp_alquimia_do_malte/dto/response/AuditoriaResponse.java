package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Auditoria;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditoriaResponse(
        UUID id,
        UUID usuarioId, String usuarioNome,
        String tabela, String acao, String observacoes,
        LocalDateTime createdAt
) {
    public static AuditoriaResponse from(Auditoria a) {
        return new AuditoriaResponse(
                a.getId(),
                a.getUsuario() != null ? a.getUsuario().getId() : null,
                a.getUsuario() != null ? a.getUsuario().getNome() : null,
                a.getTabela(), a.getAcao(), a.getObservacoes(),
                a.getCreatedAt()
        );
    }
}
