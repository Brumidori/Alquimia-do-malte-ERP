package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.LoteProduto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoteProdutoResponse(
        UUID id, String codigoLote, BigDecimal quantidade,
        LocalDate dataValidade,
        UUID produtoId, String produtoNome, String unidadeMedida,
        UUID producaoId,
        LocalDateTime createdAt
) {
    public static LoteProdutoResponse from(LoteProduto l) {
        return new LoteProdutoResponse(
                l.getId(), l.getCodigoLote(), l.getQuantidade(),
                l.getDataValidade(),
                l.getProduto().getId(), l.getProduto().getNome(), l.getProduto().getUnidadeMedida(),
                l.getProducao().getId(),
                l.getCreatedAt()
        );
    }
}
