package com.optimi.painel.domain.repository.impl;

import com.optimi.painel.domain.model.main.*;
import com.optimi.painel.domain.repository.AvertDashRepository;
import com.optimi.painel.domain.repository.HillsDashRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class HillsDashRepositoryImpl implements HillsDashRepository {

    @PersistenceContext
    EntityManager manager;

    @Override
    public List<Faturamento> buscarFaturamentoDoMesPorEstado() {
        String sql = """
                    SELECT ESTENT, SUM(DNF.VLR)
                            FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                            WHERE EXTRACT(YEAR FROM DTFAT) = EXTRACT(YEAR FROM SYSDATE)
                                  AND MES_HILLS = CASE
                                                WHEN EXTRACT(DAY FROM SYSDATE) >= 21 THEN
                                                  MOD(EXTRACT(MONTH FROM SYSDATE), 12) + 1
                                                ELSE
                                                  EXTRACT(MONTH FROM SYSDATE)
                                            END
                                  AND CODFORNEC = 11
                            GROUP BY ESTENT
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
                    WHERE ANO = EXTRACT(YEAR FROM SYSDATE)
                          AND CODFORNEC = 11
                          AND MES = CASE
                            WHEN EXTRACT(DAY FROM SYSDATE) >= 21 THEN
                              MOD(EXTRACT(MONTH FROM SYSDATE), 12) + 1
                            ELSE
                              EXTRACT(MONTH FROM SYSDATE)
                          END
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
    public List<FaturamentoMes> buscarFaturamentoRetrospec(String estado) {
        manager.createNativeQuery("ALTER SESSION SET NLS_DATE_LANGUAGE = 'PORTUGUESE'")
                .executeUpdate();

        String sql = """
                    SELECT EXTRACT(YEAR FROM DTFAT), EXTRACT(MONTH FROM DTFAT)
                           , TO_CHAR(DNF.DTFAT, 'Mon/YYYY'), ESTENT, SUM(VLR)
                    FROM OPT_NOTASFATURADAS_VIEW_MAT DNF
                    WHERE CODFORNEC = 11
                          AND ESTENT = :estado
                    GROUP BY EXTRACT(YEAR FROM DTFAT), EXTRACT(MONTH FROM DTFAT)
                           , TO_CHAR(DNF.DTFAT, 'Mon/YYYY')
                           , ESTENT
                    ORDER BY 1, 2           
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .setParameter("estado", estado)
                .getResultList();

        return resultados.stream()
                .map(linha -> new FaturamentoMes(
                        (String) linha[2],
                        (String) linha[3],
                        (BigDecimal) linha[4]
                ))
                .toList();
    }

    @Override
    public List<PioresPracasNoMes> buscarPioresPracasNoMes() {
        String sql = """
                    WITH META AS (
                      SELECT PRACA, META
                      FROM OPT_METAS
                      WHERE CODFORNEC = 11
                        AND ANO = EXTRACT(YEAR FROM SYSDATE)
                        AND MES = CASE
                                    WHEN EXTRACT(DAY FROM SYSDATE) >= 21
                                      THEN MOD(EXTRACT(MONTH FROM SYSDATE), 12) + 1
                                    ELSE EXTRACT(MONTH FROM SYSDATE)
                                  END
                        AND META > 0
                    )
                    
                    SELECT\s
                      META.PRACA,
                      CASE
                        WHEN SUM(DNF.VLR) IS NULL OR SUM(DNF.VLR) = 0 THEN 0
                        ELSE ROUND(SUM(DNF.VLR) * 100 / MAX(META.META), 2)
                      END AS PERCEN_META_ATING
                    FROM META
                      LEFT JOIN OPT_LSTRCA RCA ON RCA.PRACA = META.PRACA
                      LEFT JOIN OPT_NOTASFATURADAS_VIEW_MAT DNF ON DNF.CODUSUR = RCA.CODUSUR
                    WHERE\s
                      DNF.MES_HILLS = CASE
                                        WHEN EXTRACT(DAY FROM SYSDATE) >= 21
                                          THEN MOD(EXTRACT(MONTH FROM SYSDATE), 12) + 1
                                        ELSE EXTRACT(MONTH FROM SYSDATE)
                                      END
                      AND EXTRACT(YEAR FROM DNF.DTFAT) = EXTRACT(YEAR FROM SYSDATE)
                      AND DNF.CODFORNEC = 11
                    GROUP BY META.PRACA
                    ORDER BY 2 NULLS FIRST, 1
                    FETCH FIRST 5 ROWS ONLY       
                """;

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(linha -> new PioresPracasNoMes(
                        (String) linha[0],
                        (BigDecimal) linha[1]
                ))
                .toList();
    }
}
