package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.LoteInsumo;
import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoteInsumoRepository extends JpaRepository<LoteInsumo, UUID> {
    List<LoteInsumo> findAllByExcludedAtIsNull();

    List<LoteInsumo> findByTipoInsumoAndExcludedAtIsNullAndQuantidadeGreaterThanOrderByDataValidadeAsc(
            TipoInsumo tipoInsumo, java.math.BigDecimal quantidade);

    @Query("""
        SELECT l FROM LoteInsumo l
        WHERE l.excludedAt IS NULL
          AND l.dataValidade BETWEEN :hoje AND :limite
          AND l.quantidade > 0
        ORDER BY l.dataValidade ASC
        """)
    List<LoteInsumo> findLotesProximosAoVencimento(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite);

    @Query("SELECT COALESCE(SUM(l.quantidade), 0) FROM LoteInsumo l WHERE l.tipoInsumo.id = :tipoId AND l.excludedAt IS NULL")
    java.math.BigDecimal sumQuantidadeByTipoInsumo(@Param("tipoId") UUID tipoId);
}
