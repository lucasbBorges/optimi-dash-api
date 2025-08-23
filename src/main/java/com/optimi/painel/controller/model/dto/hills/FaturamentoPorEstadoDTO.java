package com.optimi.painel.controller.model.dto.hills;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoPorEstadoDTO {
    private String estado;
    private BigDecimal faturamento;
}
