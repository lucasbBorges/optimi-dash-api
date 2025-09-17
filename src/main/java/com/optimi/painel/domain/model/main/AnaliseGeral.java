package com.optimi.painel.domain.model.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnaliseGeral {
    private String praca;
    private BigDecimal media_atual;
    private BigDecimal perc_particip;
    private BigDecimal maior_media_hist;
    private BigDecimal perc_cresc;
    private BigDecimal perc_meta_ating;
    private BigDecimal perc_potencial_cresc;
}
