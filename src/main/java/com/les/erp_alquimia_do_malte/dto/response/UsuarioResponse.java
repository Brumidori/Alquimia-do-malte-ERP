package com.les.erp_alquimia_do_malte.dto.response;

import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.domain.enums.TipoUsuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponse(
        UUID id, String nome, String cpf, String email,
        Boolean ativo, TipoUsuario tipo,
        UUID setorId, String setorNome,
        LocalDateTime createdAt
) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getId(), u.getNome(), u.getCpf(), u.getEmail(),
                u.getAtivo(), u.getTipo(),
                u.getSetor() != null ? u.getSetor().getId() : null,
                u.getSetor() != null ? u.getSetor().getNome() : null,
                u.getCreatedAt()
        );
    }
}
