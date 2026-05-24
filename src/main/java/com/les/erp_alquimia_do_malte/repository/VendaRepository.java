package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
    List<Venda> findAllByExcludedAtIsNullOrderByDataVendaDesc();
    List<Venda> findByDataVendaBetweenAndExcludedAtIsNullOrderByDataVendaDesc(LocalDateTime inicio, LocalDateTime fim);
}
