package com.optimi.painel.domain.service;

import com.optimi.painel.domain.model.main.*;
import com.optimi.painel.domain.repository.TotalDashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<FaturamentoFornec> buscarTopFornecsMensal() {
        return totalDashRepository.buscarFaturamentoPorFornecedorMensal();
    }

    public List<FaturamentoFornec> buscarTopFornecsAnual() {
        return totalDashRepository.buscarFaturamentoPorFornecedorAnual();
    }

    public List<FaturamentoComparativo> buscarFaturamentoComparativo() {
        return totalDashRepository.buscarFaturamentoComparativo();
    }

    public List<Meta> buscarMetaAnoCorrente() {
        return totalDashRepository.buscarMetaAnoCorrente();
    }

    public List<HistoricoTotal> buscarFaturamentoAnualComparativo(Integer ano) {
        if (ano == LocalDate.now().getYear()) {
            return totalDashRepository.buscarFaturamentoAnualComparativoAnoAtual(ano);
        }
        return totalDashRepository.buscarFaturamentoAnualComparativo(ano);
    }
}
