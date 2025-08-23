package com.optimi.painel.domain.repository;

import com.optimi.painel.domain.model.main.Faturamento;
import com.optimi.painel.domain.model.main.FaturamentoPositMes;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.PioresPracasNoMes;

import java.util.List;

public interface AvertDashRepository {
    List<Faturamento> buscarFaturamentoDoMesPorEstado();
    List<Meta> buscarMetaDoMesPorEstado();
    List<FaturamentoPositMes> buscarFaturamentoRetrospec(String estado);
    List<PioresPracasNoMes> buscarPioresPracasNoMes();
}
