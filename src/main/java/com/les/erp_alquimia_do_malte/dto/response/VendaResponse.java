package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VendaResponse(
        UUID id,
        UUID clienteId, String clienteNome,
        LocalDateTime dataVenda, BigDecimal valorTotal,
        UUID usuarioId, String usuarioNome,
        List<VendaItemResponse> itens,
        LocalDateTime createdAt
) {
    public static VendaResponse from(Venda v) {
        return new VendaResponse(
                v.getId(),
                v.getCliente().getId(), v.getCliente().getNome(),
                v.getDataVenda(), v.getValorTotal(),
                v.getUsuario().getId(), v.getUsuario().getNome(),
                v.getItens().stream().map(VendaItemResponse::from).toList(),
                v.getCreatedAt()
        );
    }
}
