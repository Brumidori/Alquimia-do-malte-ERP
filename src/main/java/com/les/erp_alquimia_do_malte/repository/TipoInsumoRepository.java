package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TipoInsumoRepository extends JpaRepository<TipoInsumo, UUID> {
    List<TipoInsumo> findAllByExcludedAtIsNull();

    @Query("""
        SELECT t FROM TipoInsumo t
        WHERE t.excludedAt IS NULL
          AND t.estoqueMinimo IS NOT NULL
          AND (SELECT COALESCE(SUM(l.quantidade), 0) FROM LoteInsumo l
               WHERE l.tipoInsumo = t AND l.excludedAt IS NULL) < t.estoqueMinimo
        """)
    List<TipoInsumo> findInsumosAbaixoDoEstoqueMinimo();
}
