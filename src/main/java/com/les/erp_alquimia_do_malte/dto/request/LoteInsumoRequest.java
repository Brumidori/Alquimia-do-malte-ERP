package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoteInsumoRequest(
        @NotNull(message = "Tipo de insumo é obrigatório") UUID tipoInsumoId,
        UUID fornecedorId,
        @NotNull @Positive(message = "Quantidade deve ser positiva") BigDecimal quantidade,
        @NotNull @Future(message = "Data de validade deve ser futura") LocalDate dataValidade
) {}
