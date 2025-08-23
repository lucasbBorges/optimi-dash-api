package com.optimi.painel.domain.model.main;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoPositMes {
    private String data;
    private String estado;
    private BigDecimal faturamento;
    private BigDecimal positivacao;
}
