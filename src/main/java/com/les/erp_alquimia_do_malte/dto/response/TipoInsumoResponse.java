package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TipoInsumoResponse(
        UUID id, String nome, String unidadeMedida,
        BigDecimal estoqueMinimo, Integer validadeDias,
        LocalDateTime createdAt
) {
    public static TipoInsumoResponse from(TipoInsumo t) {
        return new TipoInsumoResponse(
                t.getId(), t.getNome(), t.getUnidadeMedida(),
                t.getEstoqueMinimo(), t.getValidadeDias(),
                t.getCreatedAt()
        );
    }
}
