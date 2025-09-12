package com.optimi.painel.domain.repository;

import com.optimi.painel.domain.model.main.*;

import java.util.List;

public interface TotalDashRepository {
    List<Faturamento> buscarFaturamentoDoMesPorEstado();
    List<Meta> buscarMetaDoMesPorEstado();
    List<HistoricoTotal> buscarFaturamentoRetrospec();
    List<FaturamentoFornec> buscarFaturamentoPorFornecedorMensal();
    List<FaturamentoFornec> buscarFaturamentoPorFornecedorAnual();
    List<FaturamentoComparativo> buscarFaturamentoComparativo();
    List<Meta> buscarMetaAnoCorrente();

}
