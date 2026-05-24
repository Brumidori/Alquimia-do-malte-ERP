package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ReceitaInsumoRequest(
        @NotNull(message = "Tipo de insumo é obrigatório") UUID tipoInsumoId,
        @NotNull @Positive(message = "Quantidade deve ser positiva") BigDecimal quantidade
) {}
