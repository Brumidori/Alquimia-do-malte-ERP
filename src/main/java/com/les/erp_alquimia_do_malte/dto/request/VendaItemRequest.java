package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record VendaItemRequest(
        @NotNull(message = "Lote do produto é obrigatório") UUID loteProdutoId,
        @NotNull @Positive(message = "Quantidade deve ser positiva") BigDecimal quantidade,
        @NotNull @Positive(message = "Valor unitário deve ser positivo") BigDecimal valorUnitario
) {}
