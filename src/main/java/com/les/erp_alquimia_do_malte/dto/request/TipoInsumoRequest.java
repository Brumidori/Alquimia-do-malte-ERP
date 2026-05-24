package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TipoInsumoRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Unidade de medida é obrigatória") String unidadeMedida,
        @Positive BigDecimal estoqueMinimo,
        @Positive Integer validadeDias
) {}
