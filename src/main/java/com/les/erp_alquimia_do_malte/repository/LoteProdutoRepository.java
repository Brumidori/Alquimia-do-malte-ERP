package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.LoteProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoteProdutoRepository extends JpaRepository<LoteProduto, UUID> {
    List<LoteProduto> findAllByExcludedAtIsNull();

    @Query("""
        SELECT l FROM LoteProduto l
        WHERE l.excludedAt IS NULL
          AND l.dataValidade BETWEEN :hoje AND :limite
          AND l.quantidade > 0
        ORDER BY l.dataValidade ASC
        """)
    List<LoteProduto> findLotesProximosAoVencimento(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite);

    @Query("SELECT COALESCE(SUM(l.quantidade), 0) FROM LoteProduto l WHERE l.produto.id = :produtoId AND l.excludedAt IS NULL AND l.quantidade > 0")
    java.math.BigDecimal sumQuantidadeByProduto(@Param("produtoId") UUID produtoId);
}
