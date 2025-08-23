package com.optimi.painel.domain.service;

import com.optimi.painel.domain.model.main.Faturamento;
import com.optimi.painel.domain.model.main.FaturamentoFornec;
import com.optimi.painel.domain.model.main.HistoricoTotal;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.repository.TotalDashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaturamentoMetaGeralService {
    @Autowired
    TotalDashRepository totalDashRepository;

    public List<Faturamento> buscarFaturamento() {
        return totalDashRepository.buscarFaturamentoDoMesPorEstado();
    }

    public List<Meta> buscarMeta() {
        return totalDashRepository.buscarMetaDoMesPorEstado();
    }

    public List<HistoricoTotal> buscarHistorico () {
        return totalDashRepository.buscarFaturamentoRetrospec();
    }

    public List<FaturamentoFornec> buscarTopFornecs() {
        return totalDashRepository.buscarFaturamentoPorFornecedor();
    }
}
