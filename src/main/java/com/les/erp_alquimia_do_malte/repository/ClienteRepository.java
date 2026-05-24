package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    List<Cliente> findAllByExcludedAtIsNull();
    boolean existsByCnpjAndExcludedAtIsNull(String cnpj);
}
