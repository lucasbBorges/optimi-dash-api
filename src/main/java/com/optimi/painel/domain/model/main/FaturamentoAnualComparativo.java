package com.optimi.painel.domain.model.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FaturamentoAnualComparativo {
    private BigDecimal faturamentoAnoAtual;
    private BigDecimal FaturamentoAnoAnterior;
}
