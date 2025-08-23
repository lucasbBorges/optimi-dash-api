package com.optimi.painel.controller;

import com.optimi.painel.controller.model.dto.*;
import com.optimi.painel.controller.model.dto.avert.AgregacaoHistoricoAvertDTO;
import com.optimi.painel.controller.model.dto.AgregacaoMetaFaturamentoDTO;
import com.optimi.painel.controller.model.dto.avert.AgregacaoPorEstadoAvertDTO;
import com.optimi.painel.controller.model.dto.avert.FaturamentoPositivacaoDTO;
import com.optimi.painel.controller.model.dto.hills.AgregacaoHistoricoHillsDTO;
import com.optimi.painel.controller.model.dto.hills.FaturamentoPorEstadoDTO;
import com.optimi.painel.domain.model.main.Faturamento;
import com.optimi.painel.domain.model.main.FaturamentoMes;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.PioresPracasNoMes;
import com.optimi.painel.domain.service.FaturamentoMetaHillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/hills")
public class HillsController {

    @Autowired
    FaturamentoMetaHillsService faturamentoMetaHillsService;

    @GetMapping("/faturamento")
    public List<AgregacaoMetaFaturamentoDTO> buscarFaturamento() {
        List<String> supervisores = new ArrayList<>(Arrays.asList("RS1","SC1","TO"));
        List<Faturamento> faturadoMesCorrente = faturamentoMetaHillsService.buscarFaturamento();
        List<Meta> metaMesCorrente = faturamentoMetaHillsService.buscarMeta();

        List<AgregacaoMetaFaturamentoDTO> agregados = new ArrayList<>();

        for (String supervisor : supervisores) {
            AgregacaoMetaFaturamentoDTO agregado = new AgregacaoMetaFaturamentoDTO();
            MetaFaturamentoDTO metaFaturamentoDTO = new MetaFaturamentoDTO();
            agregado.setEstado(supervisor);

            for (Faturamento faturamento : faturadoMesCorrente) {
                if (faturamento.getEstado().equals("RS") || faturamento.getEstado().equals("SC")) {
                    faturamento.setEstado(faturamento.getEstado().concat("1"));
                }
                if (faturamento.getEstado() != null && faturamento.getEstado().equals(supervisor)) {
                    metaFaturamentoDTO.setFaturamento(faturamento.getFaturamento());
                }
            }

            for (Meta meta : metaMesCorrente) {
                if (meta.getEstado() != null && meta.getEstado().equals(supervisor)) {
                    metaFaturamentoDTO.setMeta(meta.getMeta());
                }
            }
            agregado.setMetaFaturamento(metaFaturamentoDTO);
            agregados.add(agregado);
        }

        return agregados;
    }

    @GetMapping("/faturamento-retrospec")
    public List<AgregacaoHistoricoHillsDTO> buscarFaturamentoMes() {
        List<FaturamentoMes> faturamentoMesRs = faturamentoMetaHillsService.buscarFaturamentoRetrospectivo("RS");
        List<FaturamentoMes> faturamentoMesSc = faturamentoMetaHillsService.buscarFaturamentoRetrospectivo("SC");
        int indexIteratorMax = Math.max(faturamentoMesRs.size(), faturamentoMesSc.size());

        List<AgregacaoHistoricoHillsDTO> listaHistorico = new ArrayList<>();

        for (int i = 0; i < indexIteratorMax; i++) {
            List<FaturamentoPorEstadoDTO> faturado = new ArrayList<>();
            FaturamentoPorEstadoDTO faturadoRS = new FaturamentoPorEstadoDTO();
            FaturamentoPorEstadoDTO faturadoSC = new FaturamentoPorEstadoDTO();
            AgregacaoHistoricoHillsDTO agregadoGeral = new AgregacaoHistoricoHillsDTO();

            faturadoRS.setEstado("RS");
            if (faturamentoMesRs.size() > i) {
                faturadoRS.setFaturamento(faturamentoMesRs.get(i).getFaturamento());
            }

            faturadoSC.setEstado("SC");
            if (faturamentoMesSc.size() > i) {
                faturadoSC.setFaturamento(faturamentoMesSc.get(i).getFaturamento());
            }
            try {
                agregadoGeral.setDate(faturamentoMesRs.get(i).getData());
            } catch (ArrayIndexOutOfBoundsException e) {
                agregadoGeral.setDate(faturamentoMesSc.get(i).getData());
            }

            faturado.add(faturadoRS);
            faturado.add(faturadoSC);
            agregadoGeral.setFaturamentoPorEstadoDTO(faturado);

            listaHistorico.add(agregadoGeral);
        }

        return listaHistorico;
    }

    @GetMapping("/piores-pracas")
    public List<PioresPracasNoMes> pioresPracas() {
        return faturamentoMetaHillsService.buscarPioresPracas();
    }
}
