package com.optimi.painel.domain.repository.impl;

import com.optimi.painel.domain.model.main.*;
import com.optimi.painel.domain.repository.TotalDashRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class TotalDashRepositoryImpl implements TotalDashRepository {

    @PersistenceContext
    EntityManager manager;

    @Override
    public List<Faturamento> buscarFaturamentoDoMesPorEstado() {
        String sql = """
                    SELECT\s
                      CASE\s
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END AS ESTADO_AGRUPADO,
                      SUM(DNF.VLR) AS TOTAL
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                    WHERE\s
                      EXTRACT(YEAR FROM DNF.DTFAT) = EXTRACT(YEAR FROM SYSDATE)
                      AND EXTRACT(MONTH FROM DNF.DTFAT) = EXTRACT(MONTH FROM SYSDATE)
                    GROUP BY\s
                      CASE\s
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END
                    ORDER BY 1
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new Faturamento(
                        (String) linha[0],
                        (BigDecimal) linha[1]
                ))
                .toList();
    }

    @Override
    public List<Meta> buscarMetaDoMesPorEstado() {
        String sql = """
                    SELECT SUPERVISOR, SUM(META)
                    FROM OPT_METAS
                    WHERE ANO = EXTRACT(YEAR FROM SYSDATE) AND MES = EXTRACT(MONTH FROM SYSDATE)
                    GROUP BY SUPERVISOR
                """;
        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new Meta(
                        (String) linha[0],
                        (BigDecimal) linha[1]
                ))
                .toList();
    }

    @Override
    @Transactional
    public List<HistoricoTotal> buscarFaturamentoRetrospec() {
        manager.createNativeQuery("ALTER SESSION SET NLS_DATE_LANGUAGE = 'PORTUGUESE'")
                .executeUpdate();

        String sql = """
                    SELECT EXTRACT(YEAR FROM DTFAT), EXTRACT(MONTH FROM DTFAT)
                           , TO_CHAR(DNF.DTFAT, 'Mon/YYYY'), ROUND(SUM(VLR),2)
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                    WHERE DTFAT < TRUNC(SYSDATE, 'MM')
                    GROUP BY EXTRACT(YEAR FROM DTFAT), EXTRACT(MONTH FROM DTFAT)
                           , TO_CHAR(DNF.DTFAT, 'Mon/YYYY')
                    ORDER BY 1, 2           
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new HistoricoTotal(
                        (String) linha[2],
                        (BigDecimal) linha[3]
                ))
                .toList();
    }

    @Override
    public List<FaturamentoFornec> buscarFaturamentoPorFornecedor() {
        String sql = """
                    WITH TOP_FORNECS AS (
                    SELECT DNF.CODFORNEC, PCFORNEC.FANTASIA, ROUND(SUM(VLR),2) VLR
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                         LEFT JOIN PCFORNEC ON DNF.CODFORNEC = PCFORNEC.CODFORNEC
                    WHERE DNF.DTFAT >= SYSDATE - 30
                    GROUP BY DNF.CODFORNEC, PCFORNEC.FANTASIA
                    ORDER BY 3 DESC, 2
                    FETCH FIRST 5 ROWS ONLY
                    )
                    
                    SELECT FANTASIA, VLR
                    FROM TOP_FORNECS
                    UNION ALL
                    SELECT 'OUTROS', ROUND(SUM(VLR),2)
                    FROM OPT_NOTASFATURADAS_VIEW_MAT
                    WHERE DTFAT >= SYSDATE - 30
                          AND CODFORNEC NOT IN (SELECT CODFORNEC FROM TOP_FORNECS)
                         \s         
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new FaturamentoFornec(
                        (String) linha[0],
                        (BigDecimal) linha[1]
                ))
                .toList();
    }

    @Override
    public List<FaturamentoComparativo> buscarFaturamentoComparativo() {
        String sql = """
                    WITH
                    FATURAMENTO_ANO_ANTERIOR AS (
                    SELECT
                      CASE
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END AS ESTADO_AGRUPADO,
                      0 AS TOTAL_ANO_ATUAL,
                      ROUND(SUM(DNF.VLR),2) AS TOTAL_ANO_ANTERIOR
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                    WHERE DNF.DTFAT BETWEEN\s
                          TRUNC(ADD_MONTHS(SYSDATE, -12), 'YYYY')
                      AND ADD_MONTHS(TRUNC(SYSDATE), -12)\s
                    GROUP BY
                      CASE
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END
                    ORDER BY 1
                    ),
                    
                    FATURAMENTO_ANO_ATUAL AS (
                    SELECT
                      CASE
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END AS ESTADO_AGRUPADO,
                      ROUND(SUM(DNF.VLR),2) AS TOTAL_ANO_ATUAL,
                      0 AS TOTAL_ANO_ANTERIOR
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                    WHERE DNF.DTFAT BETWEEN\s
                          TRUNC(SYSDATE, 'YYYY')
                      AND TRUNC(SYSDATE)\s
                    GROUP BY
                      CASE
                        WHEN DNF.ESTENT IN ('TO', 'PA') THEN 'TO'
                        ELSE DNF.ESTENT
                      END
                    ORDER BY 1
                    )
                    , CONSULTA AS(
                    SELECT *
                    FROM FATURAMENTO_ANO_ANTERIOR
                    UNION ALL
                    SELECT *
                    FROM FATURAMENTO_ANO_ATUAL
                    )
                    
                    SELECT ESTADO_AGRUPADO, SUM(TOTAL_ANO_ATUAL), SUM(TOTAL_ANO_ANTERIOR)
                    FROM CONSULTA
                    GROUP BY ESTADO_AGRUPADO
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new FaturamentoComparativo(
                        (String) linha[0],
                        (BigDecimal) linha[1],
                        (BigDecimal) linha[2]
                ))
                .toList();
    }

    @Override
    public List<Meta> buscarMetaAnoCorrente() {
        String sql = """
                    SELECT SUPERVISOR, SUM(META)
                    FROM OPT_METAS
                    WHERE ANO = EXTRACT(YEAR FROM SYSDATE) AND MES <= EXTRACT(MONTH FROM SYSDATE)
                    GROUP BY SUPERVISOR
                """;
        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new Meta(
                        (String) linha[0],
                        (BigDecimal) linha[1]
                ))
                .toList();
    }
}
