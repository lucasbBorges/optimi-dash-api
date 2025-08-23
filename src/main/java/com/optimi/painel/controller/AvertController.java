package com.optimi.painel.controller;

import com.optimi.painel.controller.model.dto.*;
import com.optimi.painel.controller.model.dto.avert.AgregacaoHistoricoAvertDTO;
import com.optimi.painel.controller.model.dto.AgregacaoMetaFaturamentoDTO;
import com.optimi.painel.controller.model.dto.avert.AgregacaoPorEstadoAvertDTO;
import com.optimi.painel.controller.model.dto.avert.FaturamentoPositivacaoDTO;
import com.optimi.painel.domain.model.main.Faturamento;
import com.optimi.painel.domain.model.main.FaturamentoPositMes;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.PioresPracasNoMes;
import com.optimi.painel.domain.service.FaturamentoMetaAvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/avert")
public class AvertController {

    @Autowired
    FaturamentoMetaAvertService faturamentoMetaAvertService;

    @GetMapping("/faturamento")
    public List<AgregacaoMetaFaturamentoDTO> buscarFaturamento() {
        List<String> supervisores = new ArrayList<>(Arrays.asList("RS1","SC1","TO"));
        List<Faturamento> faturadoMesCorrente = faturamentoMetaAvertService.buscarFaturamento();
        List<Meta> metaMesCorrente = faturamentoMetaAvertService.buscarMeta();

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
    public List<AgregacaoHistoricoAvertDTO> buscarFaturamentoMes() {
        List<FaturamentoPositMes> faturamentoPositMesRs =  faturamentoMetaAvertService.buscarFaturamentoRetrospectivo("RS");
        List<FaturamentoPositMes> faturamentoPositMesSc =  faturamentoMetaAvertService.buscarFaturamentoRetrospectivo("SC");

        int indexIteratorMax = Math.max(faturamentoPositMesRs.size(), faturamentoPositMesSc.size());

        List<AgregacaoHistoricoAvertDTO> listaHistorico = new ArrayList<>();

        for (int i=0; i < indexIteratorMax; i++) {
            List<AgregacaoPorEstadoAvertDTO> agregadoGeral = new ArrayList<>();
            AgregacaoHistoricoAvertDTO historico = new AgregacaoHistoricoAvertDTO();

            AgregacaoPorEstadoAvertDTO agregadoRs =  new AgregacaoPorEstadoAvertDTO();
            FaturamentoPositivacaoDTO faturadoRs = new FaturamentoPositivacaoDTO();

            if (faturamentoPositMesRs.size() > i) {
                faturadoRs.setFaturamento(faturamentoPositMesRs.get(i).getFaturamento());
                faturadoRs.setPositivacao(faturamentoPositMesRs.get(i).getPositivacao());
            }
            agregadoRs.setFaturamentoPositivacaoDTO(faturadoRs);
            agregadoRs.setEstado("RS");
            agregadoGeral.add(agregadoRs);


            AgregacaoPorEstadoAvertDTO agregadosSc = new AgregacaoPorEstadoAvertDTO();
            FaturamentoPositivacaoDTO faturadoSc = new FaturamentoPositivacaoDTO();

            if (faturamentoPositMesSc.size() > i) {
                faturadoSc.setFaturamento(faturamentoPositMesSc.get(i).getFaturamento());
                faturadoSc.setPositivacao(faturamentoPositMesSc.get(i).getPositivacao());
            }

            agregadosSc.setFaturamentoPositivacaoDTO(faturadoSc);
            agregadosSc.setEstado("SC");
            agregadoGeral.add(agregadosSc);

            historico.setData(faturamentoPositMesRs.get(i).getData());
            historico.setAgregacaoPorEstadoAvert(agregadoGeral);
            listaHistorico.add(historico);
        }

        return listaHistorico;
    }

    @GetMapping("/piores-pracas")
    public List<PioresPracasNoMes> pioresPracas() {
        return faturamentoMetaAvertService.buscarPioresPracas();
    }
}
