package com.optimi.painel.domain.model.main;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Faturamento {
    private String estado;
    private BigDecimal faturamento;
}
