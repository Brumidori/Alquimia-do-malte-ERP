package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Producao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProducaoResponse(
        UUID id, BigDecimal quantidadeProduzida, LocalDateTime dataProducao,
        UUID receitaId, String receitaNome,
        UUID produtoId, String produtoNome,
        UUID usuarioId, String usuarioNome
) {
    public static ProducaoResponse from(Producao p) {
        return new ProducaoResponse(
                p.getId(), p.getQuantidadeProduzida(), p.getDataProducao(),
                p.getReceita().getId(), p.getReceita().getNome(),
                p.getReceita().getProduto().getId(), p.getReceita().getProduto().getNome(),
                p.getUsuario().getId(), p.getUsuario().getNome()
        );
    }
}
