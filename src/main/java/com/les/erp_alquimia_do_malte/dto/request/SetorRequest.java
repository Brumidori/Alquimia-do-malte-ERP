package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SetorRequest(
        @NotBlank(message = "Nome é obrigatório") String nome
) {}
