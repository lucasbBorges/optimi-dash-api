package com.optimi.painel.controller.model.dto.hills;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgregacaoHistoricoHillsDTO {
    private String date;
    private List<FaturamentoPorEstadoDTO> faturamentoPorEstadoDTO;
}
