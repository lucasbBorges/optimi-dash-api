package com.optimi.painel.controller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaFaturamentoDTO {
    BigDecimal faturamento;
    BigDecimal meta;
}
