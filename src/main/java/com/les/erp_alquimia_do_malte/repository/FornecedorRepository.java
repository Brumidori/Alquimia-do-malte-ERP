package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
    List<Fornecedor> findAllByExcludedAtIsNull();
    boolean existsByCnpjAndExcludedAtIsNull(String cnpj);
}
