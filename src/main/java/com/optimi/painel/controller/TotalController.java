package com.optimi.painel.controller;

import com.optimi.painel.controller.model.dto.AgregacaoMetaFaturamentoDTO;
import com.optimi.painel.controller.model.dto.MetaFaturamentoDTO;
import com.optimi.painel.controller.model.dto.comparativo.AgregacaoFaturadoAnualComparativo;
import com.optimi.painel.controller.model.dto.comparativo.AgregacaoMetaFaturamentoComparativoDTO;
import com.optimi.painel.controller.model.dto.comparativo.MetaFaturamentoComparativoDTO;
import com.optimi.painel.domain.model.main.*;
import com.optimi.painel.domain.service.FaturamentoMetaGeralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/total")
public class TotalController {

    @Autowired
    FaturamentoMetaGeralService faturamentoMetaGeralService;

    @GetMapping("/faturamento")
    public List<AgregacaoMetaFaturamentoDTO> metaFaturamento() {
        List<String> supervisores = new ArrayList<>(Arrays.asList("RS1","SC1","TO"));
        List<Faturamento> faturadoMesCorrente = faturamentoMetaGeralService.buscarFaturamento();
        List<Meta> metaMesCorrente = faturamentoMetaGeralService.buscarMeta();

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

    @GetMapping("/faturamento-comparativo")
    public List<AgregacaoMetaFaturamentoComparativoDTO> listarFaturamentoComparativo() {
        List<String> supervisores = new ArrayList<>(Arrays.asList("RS1","SC1","TO"));
        List<FaturamentoComparativo> faturadoComparativo = faturamentoMetaGeralService.buscarFaturamentoComparativo();
        List<Meta> metaMesCorrente = faturamentoMetaGeralService.buscarMetaAnoCorrente();

        List<AgregacaoMetaFaturamentoComparativoDTO> agregados = new ArrayList<>();

        for (String supervisor : supervisores) {
            AgregacaoMetaFaturamentoComparativoDTO agregado = new AgregacaoMetaFaturamentoComparativoDTO();
            MetaFaturamentoComparativoDTO metaFaturamentoDTO = new MetaFaturamentoComparativoDTO();
            agregado.setEstado(supervisor);

            for (FaturamentoComparativo faturamento : faturadoComparativo) {
                if (faturamento.getEstado().equals("RS") || faturamento.getEstado().equals("SC")) {
                    faturamento.setEstado(faturamento.getEstado().concat("1"));
                }
                if (faturamento.getEstado() != null && faturamento.getEstado().equals(supervisor)) {
                    metaFaturamentoDTO.setFaturamentoAnoAtual(faturamento.getFaturamentoAnoAtual());
                    metaFaturamentoDTO.setFaturamentoAnoAnterior(faturamento.getFaturamentoAnoAnterior());
                }
            }

            for (Meta meta : metaMesCorrente) {
                if (meta.getEstado() != null && meta.getEstado().equals(supervisor)) {
                    metaFaturamentoDTO.setMeta(meta.getMeta());
                }
            }
            agregado.setMetaFaturamentoComparativoDTO(metaFaturamentoDTO);
            agregados.add(agregado);
        }
        return agregados;
    }

    @GetMapping("/faturamento-retrospec")
    public List<HistoricoTotal> buscarHistorico () {
        return faturamentoMetaGeralService.buscarHistorico();
    }

    @GetMapping("/top-fornec-mensal")
    public List<FaturamentoFornec> topFornecsMensal() { return faturamentoMetaGeralService.buscarTopFornecsMensal(); }

    @GetMapping("/top-fornec-anual")
    public List<FaturamentoFornec> topFornecsAnual() { return faturamentoMetaGeralService.buscarTopFornecsAnual(); }

    @GetMapping("/faturamento-anual-comparativo")
    public AgregacaoFaturadoAnualComparativo buscarFaturamentoAnualComparativo(
                                                                  @RequestParam(required = false) Integer ano1,
                                                                  @RequestParam(required = false) Integer ano2) {

        AgregacaoFaturadoAnualComparativo agregacao = new AgregacaoFaturadoAnualComparativo();
        List<HistoricoTotal> faturadoAnoPassado;
        List<HistoricoTotal> faturadoAnoRecente;

        if (ano1 == null || ano2 == null) {
            ano1 = LocalDate.now().getYear();
            ano2 = LocalDate.now().getYear() - 1;

            faturadoAnoRecente =  faturamentoMetaGeralService.buscarFaturamentoAnualComparativo(ano1);
            faturadoAnoPassado =  faturamentoMetaGeralService.buscarFaturamentoAnualComparativo(ano2);

            agregacao.setFaturamentoPassado(faturadoAnoPassado);
            agregacao.setFaturamentoRecente(faturadoAnoRecente);
            return agregacao;
        }

        Integer anoPassado = ano1 <= ano2 ? ano1 : ano2;
        Integer anoRecente = ano1 >= ano2 ? ano1 : ano2;

        faturadoAnoPassado = faturamentoMetaGeralService.buscarFaturamentoAnualComparativo(anoPassado);
        faturadoAnoRecente = faturamentoMetaGeralService.buscarFaturamentoAnualComparativo(anoRecente);
        agregacao.setFaturamentoRecente(faturadoAnoRecente);
        agregacao.setFaturamentoPassado(faturadoAnoPassado);

        return agregacao;
    }
}
