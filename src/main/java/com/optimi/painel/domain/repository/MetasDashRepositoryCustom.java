package com.optimi.painel.domain.repository;

import com.optimi.painel.controller.model.dto.meta.MetaDto;
import com.optimi.painel.controller.model.dto.meta.MetaRequest;
import com.optimi.painel.domain.model.main.Praca;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MetasDashRepositoryCustom {
    List<Praca> listarPracas ();
    Slice<MetaDto> listarMetaMesCorrente(Pageable pageable, MetaRequest metaRequest);
}
