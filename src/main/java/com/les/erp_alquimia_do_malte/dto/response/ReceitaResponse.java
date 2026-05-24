package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Receita;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReceitaResponse(
        UUID id, String nome,
        UUID produtoId, String produtoNome,
        List<ReceitaInsumoResponse> insumos,
        LocalDateTime createdAt
) {
    public static ReceitaResponse from(Receita r) {
        return new ReceitaResponse(
                r.getId(), r.getNome(),
                r.getProduto().getId(), r.getProduto().getNome(),
                r.getInsumos().stream().map(ReceitaInsumoResponse::from).toList(),
                r.getCreatedAt()
        );
    }
}
