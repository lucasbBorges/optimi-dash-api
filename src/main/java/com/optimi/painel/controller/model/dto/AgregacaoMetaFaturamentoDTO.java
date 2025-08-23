package com.optimi.painel.controller.model.dto;

import com.optimi.painel.controller.model.dto.MetaFaturamentoDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AgregacaoMetaFaturamentoDTO {
    private String estado;
    private MetaFaturamentoDTO metaFaturamento;
}
