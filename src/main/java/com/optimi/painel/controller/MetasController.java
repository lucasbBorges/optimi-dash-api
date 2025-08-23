package com.optimi.painel.controller;

import com.optimi.painel.controller.model.dto.SliceResponse;
import com.optimi.painel.controller.model.dto.meta.MetaDto;
import com.optimi.painel.controller.model.dto.meta.MetaRequest;
import com.optimi.painel.controller.model.dto.meta.MetaUpdateRequest;
import com.optimi.painel.domain.model.main.Meta;
import com.optimi.painel.domain.model.main.MetaTabela;
import com.optimi.painel.domain.model.main.Praca;
import com.optimi.painel.domain.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
public class MetasController {
    @Autowired
    MetaService metaService;

    @GetMapping("/listar-pracas")
    public List<Praca> listarPracas () {
        return metaService.listarPracas();
    }

    @GetMapping("/listar-meta-mes-corrente")
    public SliceResponse<MetaDto> listarMesCorrenteSlice(@PageableDefault(size = 20) Pageable pageable,
                                                         @RequestParam(name = "ano", required = false) Integer ano,
                                                         @RequestParam(name = "mes", required = false) Integer mes,
                                                         @RequestParam(name = "supervisor", required = false) String supervisor,
                                                         @RequestParam(name = "praca", required = false) String praca) {
        MetaRequest metaRequest = new MetaRequest(ano, mes, supervisor, praca);
        var s = metaService.listarMetaMesCorrente(pageable, metaRequest);
        return new SliceResponse<>(s.getContent(), s.getNumber(), s.getSize(), s.hasPrevious(), s.hasNext());
    }

    @PostMapping
    public ResponseEntity<MetaDto> cadastrarMeta(@RequestBody MetaTabela meta) {
        MetaDto metaDto = metaService.cadastrarMeta(meta);
        return ResponseEntity
                .created(java.net.URI.create("/metas/" + metaDto.id()))
                .body(metaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaDto> alterarMeta(@PathVariable Long id,
                                               @RequestBody MetaUpdateRequest metaUpdateRequest) {
        MetaDto metaDto = metaService.alterarMeta(id, metaUpdateRequest.value());
        return ResponseEntity
                .created(java.net.URI.create("/metas/" + metaDto.id()))
                .body(metaDto);
    }

    @DeleteMapping("/{id}")
    public void deletarMeta(@PathVariable Long id) {
        metaService.deletarMeta(id);
    }
}
