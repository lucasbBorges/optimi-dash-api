package com.optimi.painel.domain.service;

import com.optimi.painel.domain.model.main.*;
import com.optimi.painel.domain.repository.AvertDashRepository;
import com.optimi.painel.domain.repository.HillsDashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaturamentoMetaHillsService {
    @Autowired
    HillsDashRepository hillsDashRepository;

    public List<Faturamento> buscarFaturamento() {
        return hillsDashRepository.buscarFaturamentoDoMesPorEstado();
    }

    public List<Meta> buscarMeta() {
        return hillsDashRepository.buscarMetaDoMesPorEstado();
    }

    public List<FaturamentoMes> buscarFaturamentoRetrospectivo(String estado) {
        return hillsDashRepository.buscarFaturamentoRetrospec(estado);
    }

    public List<PioresPracasNoMes> buscarPioresPracas() {
        return hillsDashRepository.buscarPioresPracasNoMes();
    }
}
