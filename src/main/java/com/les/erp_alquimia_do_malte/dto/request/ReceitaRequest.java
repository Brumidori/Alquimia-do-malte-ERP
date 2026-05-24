package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ReceitaRequest(
        @NotNull(message = "Produto é obrigatório") UUID produtoId,
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotEmpty(message = "A receita deve ter pelo menos um insumo") @Valid List<ReceitaInsumoRequest> insumos
) {}
