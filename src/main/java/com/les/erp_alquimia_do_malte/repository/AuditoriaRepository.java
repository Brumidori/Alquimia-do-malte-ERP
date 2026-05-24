package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditoriaRepository extends JpaRepository<Auditoria, UUID> {
    Page<Auditoria> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Auditoria> findByTabelaAndCreatedAtBetweenOrderByCreatedAtDesc(String tabela, LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
    Page<Auditoria> findByUsuario_IdOrderByCreatedAtDesc(UUID usuarioId, Pageable pageable);
}
