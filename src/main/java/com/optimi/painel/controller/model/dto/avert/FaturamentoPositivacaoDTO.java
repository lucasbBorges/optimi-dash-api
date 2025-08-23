package com.optimi.painel.controller.model.dto.avert;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FaturamentoPositivacaoDTO {
    private BigDecimal faturamento;
    private BigDecimal positivacao;
}
