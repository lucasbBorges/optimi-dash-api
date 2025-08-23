package com.optimi.painel.domain.model.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoTotal {
    private String date;
    private BigDecimal faturamento;
}
