package com.les.erp_alquimia_do_malte.dto.request;

import com.les.erp_alquimia_do_malte.domain.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "CPF é obrigatório") String cpf,
        @NotBlank @Email(message = "E-mail inválido") String email,
        @NotBlank(message = "Senha é obrigatória") String senha,
        @NotNull(message = "Tipo é obrigatório") TipoUsuario tipo,
        UUID setorId
) {}
