package com.optimi.painel.controller.model.dto.comparativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaFaturamentoComparativoDTO {
    private BigDecimal faturamentoAnoAtual;
    private BigDecimal faturamentoAnoAnterior;
    private BigDecimal meta;
}
