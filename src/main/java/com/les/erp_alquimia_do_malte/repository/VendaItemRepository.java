package com.les.erp_alquimia_do_malte.repository;

import com.les.erp_alquimia_do_malte.domain.entity.VendaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendaItemRepository extends JpaRepository<VendaItem, UUID> {
}
