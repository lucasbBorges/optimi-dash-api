package com.optimi.painel.controller.model.dto.comparativo;

import com.optimi.painel.domain.model.main.HistoricoTotal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregacaoFaturadoAnualComparativo {
    private List<HistoricoTotal> faturamentoPassado;
    private List<HistoricoTotal> faturamentoRecente;

}
