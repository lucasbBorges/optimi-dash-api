package com.optimi.painel.domain.repository;

import com.optimi.painel.domain.model.main.*;

import java.util.List;

public interface HillsDashRepository {
    List<Faturamento> buscarFaturamentoDoMesPorEstado();
    List<Meta> buscarMetaDoMesPorEstado();
    List<FaturamentoMes> buscarFaturamentoRetrospec(String estado);
    List<PioresPracasNoMes> buscarPioresPracasNoMes();
}
