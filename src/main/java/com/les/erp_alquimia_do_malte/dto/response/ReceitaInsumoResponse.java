package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.ReceitaInsumo;

import java.math.BigDecimal;
import java.util.UUID;

public record ReceitaInsumoResponse(UUID id, UUID tipoInsumoId, String tipoInsumoNome, String unidadeMedida, BigDecimal quantidade) {
    public static ReceitaInsumoResponse from(ReceitaInsumo ri) {
        return new ReceitaInsumoResponse(
                ri.getId(),
                ri.getTipoInsumo().getId(),
                ri.getTipoInsumo().getNome(),
                ri.getTipoInsumo().getUnidadeMedida(),
                ri.getQuantidade()
        );
    }
}
