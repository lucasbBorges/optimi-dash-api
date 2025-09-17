package com.optimi.painel.controller;

import com.optimi.painel.domain.model.main.AnaliseGeral;
import com.optimi.painel.domain.service.AnaliseGeralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/analise-geral")
@RestController
public class AnaliseGeralController {

    @Autowired
    AnaliseGeralService analiseGeralService;

    @GetMapping
    public List<AnaliseGeral> exibirAnaliseGeral () {
        return analiseGeralService.gerarAnaliseGeral();
    }
}
