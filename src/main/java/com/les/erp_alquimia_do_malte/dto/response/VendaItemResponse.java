package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.VendaItem;

import java.math.BigDecimal;
import java.util.UUID;

public record VendaItemResponse(
        UUID id,
        UUID loteProdutoId, String codigoLote,
        String produtoNome,
        BigDecimal quantidade, BigDecimal valorUnitario, BigDecimal subtotal
) {
    public static VendaItemResponse from(VendaItem vi) {
        return new VendaItemResponse(
                vi.getId(),
                vi.getLoteProduto().getId(), vi.getLoteProduto().getCodigoLote(),
                vi.getLoteProduto().getProduto().getNome(),
                vi.getQuantidade(), vi.getValorUnitario(),
                vi.getQuantidade().multiply(vi.getValorUnitario())
        );
    }
}
