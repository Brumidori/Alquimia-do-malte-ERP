package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReceitaRepository extends JpaRepository<Receita, UUID> {
    List<Receita> findAllByExcludedAtIsNull();
}
