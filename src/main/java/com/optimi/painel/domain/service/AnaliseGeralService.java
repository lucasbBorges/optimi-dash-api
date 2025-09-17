package com.optimi.painel.domain.service;

import com.optimi.painel.domain.model.main.AnaliseGeral;
import com.optimi.painel.domain.repository.AnaliseGeralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnaliseGeralService {

    @Autowired
    AnaliseGeralRepository analiseGeralRepository;

    public List<AnaliseGeral> gerarAnaliseGeral () {
        return analiseGeralRepository.analiseGeral();
    }
}
