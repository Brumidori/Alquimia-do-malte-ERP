package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.LoteInsumo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoteInsumoResponse(
        UUID id, String codigoLote, BigDecimal quantidade,
        LocalDate dataValidade,
        UUID tipoInsumoId, String tipoInsumoNome, String unidadeMedida,
        UUID fornecedorId, String fornecedorNome,
        LocalDateTime createdAt
) {
    public static LoteInsumoResponse from(LoteInsumo l) {
        return new LoteInsumoResponse(
                l.getId(), l.getCodigoLote(), l.getQuantidade(),
                l.getDataValidade(),
                l.getTipoInsumo().getId(), l.getTipoInsumo().getNome(), l.getTipoInsumo().getUnidadeMedida(),
                l.getFornecedor() != null ? l.getFornecedor().getId() : null,
                l.getFornecedor() != null ? l.getFornecedor().getNome() : null,
                l.getCreatedAt()
        );
    }
}
