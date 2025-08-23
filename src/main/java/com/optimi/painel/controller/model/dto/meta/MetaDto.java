package com.optimi.painel.controller.model.dto.meta;

import java.math.BigDecimal;

public record MetaDto(
        Long id,
        Integer ano,
        Integer mes,
        String supervisor,
        String praca,
        Long codfornec,
        BigDecimal meta,
        String fantasia
) {}
