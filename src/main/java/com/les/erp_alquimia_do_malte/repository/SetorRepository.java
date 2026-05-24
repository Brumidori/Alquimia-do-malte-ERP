package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SetorRepository extends JpaRepository<Setor, UUID> {
    List<Setor> findAllByExcludedAtIsNull();
    boolean existsByNomeIgnoreCaseAndExcludedAtIsNull(String nome);
}
