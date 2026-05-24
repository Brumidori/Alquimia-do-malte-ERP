package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VendaRequest(
        @NotNull(message = "Cliente é obrigatório") UUID clienteId,
        @NotNull(message = "Data da venda é obrigatória") LocalDateTime dataVenda,
        @NotEmpty(message = "A venda deve ter pelo menos um item") @Valid List<VendaItemRequest> itens
) {}
