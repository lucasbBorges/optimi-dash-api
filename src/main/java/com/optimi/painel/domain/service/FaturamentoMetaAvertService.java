package com.optimi.painel.domain.service;

import com.optimi.painel.domain.model.main.Faturamento;
import com.optimi.painel.domain.model.main.FaturamentoPositMes;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.PioresPracasNoMes;
import com.optimi.painel.domain.repository.AvertDashRepository;
import com.optimi.painel.domain.repository.TotalDashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaturamentoMetaAvertService {
    @Autowired
    AvertDashRepository avertDashRepository;

    public List<Faturamento> buscarFaturamento() {
        return avertDashRepository.buscarFaturamentoDoMesPorEstado();
    }

    public List<Meta> buscarMeta() {
        return avertDashRepository.buscarMetaDoMesPorEstado();
    }

    public List<FaturamentoPositMes> buscarFaturamentoRetrospectivo(String estado) {
        return avertDashRepository.buscarFaturamentoRetrospec(estado);
    }

    public List<PioresPracasNoMes> buscarPioresPracas() {
        return avertDashRepository.buscarPioresPracasNoMes();
    }
}
