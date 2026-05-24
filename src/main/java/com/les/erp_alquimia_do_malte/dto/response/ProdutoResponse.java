package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Produto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProdutoResponse(UUID id, String nome, String unidadeMedida, Integer validadeDias, LocalDateTime createdAt) {
    public static ProdutoResponse from(Produto p) {
        return new ProdutoResponse(p.getId(), p.getNome(), p.getUnidadeMedida(), p.getValidadeDias(), p.getCreatedAt());
    }
}
