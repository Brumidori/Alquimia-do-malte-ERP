package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Unidade de medida é obrigatória") String unidadeMedida,
        @NotNull @Positive(message = "Validade em dias deve ser positiva") Integer validadeDias
) {}
