package com.optimi.painel.controller.model.dto.meta;

public record MetaRequest (
        Integer ano,
        Integer mes,
        String supervisor,
        String praca
) {}