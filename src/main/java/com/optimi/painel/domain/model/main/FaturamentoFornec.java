package com.optimi.painel.domain.model.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturamentoFornec {
    private String fornec;
    private BigDecimal faturamento;
}
