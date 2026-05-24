package com.les.erp_alquimia_do_malte.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lotes_produto")
public class LoteProduto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producao_id", nullable = false)
    private Producao producao;

    @Column(name = "codigo_lote", nullable = false, unique = true)
    private String codigoLote;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;
}
