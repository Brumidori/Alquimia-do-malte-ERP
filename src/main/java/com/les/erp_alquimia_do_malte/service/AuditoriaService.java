package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Auditoria;
import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.dto.response.AuditoriaResponse;
import com.les.erp_alquimia_do_malte.repository.AuditoriaRepository;
import com.les.erp_alquimia_do_malte.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository, UsuarioRepository usuarioRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(UUID usuarioId, String tabela, String acao, String observacoes) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTabela(tabela);
        auditoria.setAcao(acao);
        auditoria.setObservacoes(observacoes);
        if (usuarioId != null) {
            usuarioRepository.findById(usuarioId).ifPresent(auditoria::setUsuario);
        }
        auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> listar(Pageable pageable) {
        return auditoriaRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(AuditoriaResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> filtrar(String tabela, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        return auditoriaRepository.findByTabelaAndCreatedAtBetweenOrderByCreatedAtDesc(tabela, inicio, fim, pageable)
                .map(AuditoriaResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> porUsuario(UUID usuarioId, Pageable pageable) {
        return auditoriaRepository.findByUsuario_IdOrderByCreatedAtDesc(usuarioId, pageable)
                .map(AuditoriaResponse::from);
    }
}
