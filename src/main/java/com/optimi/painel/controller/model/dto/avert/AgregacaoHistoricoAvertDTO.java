package com.optimi.painel.controller.model.dto.avert;

import lombok.Data;

import java.util.List;

@Data
public class AgregacaoHistoricoAvertDTO {
    private String data;
    private List<AgregacaoPorEstadoAvertDTO> agregacaoPorEstadoAvert;
}
