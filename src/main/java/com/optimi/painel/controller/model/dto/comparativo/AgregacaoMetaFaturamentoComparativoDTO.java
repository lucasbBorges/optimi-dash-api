package com.optimi.painel.controller.model.dto.comparativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregacaoMetaFaturamentoComparativoDTO {
    String estado;
    MetaFaturamentoComparativoDTO metaFaturamentoComparativoDTO;
}
