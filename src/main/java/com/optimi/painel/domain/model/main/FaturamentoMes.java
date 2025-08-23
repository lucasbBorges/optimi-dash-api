package com.optimi.painel.domain.model.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoMes {
    private String data;
    private String estado;
    private BigDecimal faturamento;
}
