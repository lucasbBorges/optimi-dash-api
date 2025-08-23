package com.optimi.painel.domain.model.main;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "OPT_METAS")
public class MetaTabela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SUPERVISOR")
    private String supervisor;

    @Column(name = "PRACA")
    private String praca;

    @Column(name = "ANO")
    private Integer ano;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "CODFORNEC")
    private Long codfornec;

    @Column(name = "META")
    private BigDecimal meta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODFORNEC", referencedColumnName = "CODFORNEC", insertable = false, updatable = false)
    private Fornecedor fornecedor;
}
