package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProducaoRequest(
        @NotNull(message = "Receita é obrigatória") UUID receitaId,
        @NotNull @Positive(message = "Quantidade produzida deve ser positiva") BigDecimal quantidadeProduzida,
        @NotNull(message = "Data de produção é obrigatória") LocalDateTime dataProducao
) {}
