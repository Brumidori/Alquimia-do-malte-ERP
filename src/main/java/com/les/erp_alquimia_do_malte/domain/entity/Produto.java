package com.les.erp_alquimia_do_malte.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @Column(name = "validade_dias", nullable = false)
    private Integer validadeDias;
}
