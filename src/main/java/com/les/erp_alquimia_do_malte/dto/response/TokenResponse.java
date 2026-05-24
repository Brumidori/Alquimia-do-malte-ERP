package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.enums.TipoUsuario;

import java.util.UUID;

public record TokenResponse(
        String token,
        String tipo,
        UUID usuarioId,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        UUID setorId
) {}
