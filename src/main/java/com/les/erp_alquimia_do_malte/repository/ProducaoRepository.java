package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Producao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProducaoRepository extends JpaRepository<Producao, UUID> {
    List<Producao> findAllByOrderByDataProducaoDesc();
    List<Producao> findByDataProducaoBetweenOrderByDataProducaoDesc(LocalDateTime inicio, LocalDateTime fim);
}
