package com.optimi.painel.domain.model.main;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PCFORNEC")
public class Fornecedor {
    @Id
    @Column(name = "CODFORNEC", nullable = false)
    private Long codfornec;

    @Column(name = "FANTASIA")
    private String fantasia;
}
