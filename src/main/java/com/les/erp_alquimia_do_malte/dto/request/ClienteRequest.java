package com.les.erp_alquimia_do_malte.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        String cnpj,
        String telefone,
        String email
) {}
