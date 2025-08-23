package com.optimi.painel.domain.service;

import com.optimi.painel.controller.model.dto.meta.MetaDto;
import com.optimi.painel.controller.model.dto.meta.MetaRequest;
import com.optimi.painel.domain.model.main.Fornecedor;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.MetaTabela;
import com.optimi.painel.domain.model.main.Praca;
import com.optimi.painel.domain.repository.MetasDashRepository;
import com.optimi.painel.domain.repository.PcfornecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MetaService {
    @Autowired
    MetasDashRepository metasDashRepository;

    @Autowired
    PcfornecRepository pcfornecRepository;

    public List<Praca> listarPracas () {
        return metasDashRepository.listarPracas();
    }
    public Slice<MetaDto> listarMetaMesCorrente(Pageable pageable, MetaRequest metaRequest) {
        return metasDashRepository.listarMetaMesCorrente(pageable, metaRequest);
    }

    @Transactional
    public MetaDto cadastrarMeta(MetaTabela meta) {
        Fornecedor fornec = pcfornecRepository.findById(meta.getCodfornec())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        MetaTabela novaMeta = metasDashRepository.save(meta);
        meta.setFornecedor(fornec);
        return new MetaDto(novaMeta.getId(), novaMeta.getAno(), novaMeta.getMes(), novaMeta.getSupervisor(), novaMeta.getPraca()
                , novaMeta.getCodfornec(),novaMeta.getMeta(), novaMeta.getFornecedor().getFantasia());
    }

    @Transactional
    public MetaDto alterarMeta(Long id, BigDecimal value){
        MetaTabela metaTabela = metasDashRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar meta"));

        metaTabela.setMeta(value);
        return new MetaDto(metaTabela.getId(), metaTabela.getAno(), metaTabela.getMes(), metaTabela.getSupervisor(),
                metaTabela.getPraca(), metaTabela.getCodfornec(), metaTabela.getMeta(), metaTabela.getFornecedor().getFantasia());
    }

    @Transactional
    public void deletarMeta(Long id) {
        metasDashRepository.deleteById(id);
    }
}
