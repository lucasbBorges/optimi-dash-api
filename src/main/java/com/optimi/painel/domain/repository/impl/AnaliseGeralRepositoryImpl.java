package com.optimi.painel.domain.repository.impl;

import com.optimi.painel.domain.model.main.AnaliseGeral;
import com.optimi.painel.domain.model.main.HistoricoTotal;
import com.optimi.painel.domain.repository.AnaliseGeralRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class AnaliseGeralRepositoryImpl implements AnaliseGeralRepository {

    @PersistenceContext
    EntityManager manager;

    @Override
    public List<AnaliseGeral> analiseGeral() {
        String sql = "WITH\n" +
                "MAIOR_MEDIA_HISTORICA AS (\n" +
                "    SELECT EXTRACT(YEAR FROM DTFAT) ANO\n" +
                "           , REPLACE(PRACA_VENDA, ' ','') PRACA\n" +
                "           , ROUND(SUM(VALOR_1464) / 12, 2) MEDIA\n" +
                "    FROM OPT_NOTASFATURADAS_VIEW_MAT\n" +
                "    WHERE EXTRACT(YEAR FROM DTFAT) < EXTRACT(YEAR FROM SYSDATE)\n" +
                "    GROUP BY EXTRACT(YEAR FROM DTFAT), PRACA_VENDA\n" +
                "    ORDER BY 3 DESC\n" +
                ")\n" +
                "\n" +
                ", MEDIA_ATUAL AS (\n" +
                "    SELECT REPLACE(PRACA_VENDA, ' ','') PRACA, \n" +
                "      CASE\n" +
                "       WHEN EXTRACT(MONTH FROM SYSDATE) = 1 THEN 0\n" +
                "       ELSE ROUND(SUM(VALOR_1464) / (EXTRACT(MONTH FROM SYSDATE) - 1),2)\n" +
                "      END MEDIA\n" +
                "      , SUM(VALOR_1464) FAT_ATUAL\n" +
                "     FROM OPT_NOTASFATURADAS_VIEW_MAT\n" +
                "     WHERE EXTRACT(YEAR FROM DTFAT) = EXTRACT(YEAR FROM SYSDATE)\n" +
                "           AND EXTRACT(MONTH FROM DTFAT) < EXTRACT(MONTH FROM SYSDATE)\n" +
                "     GROUP BY PRACA_VENDA\n" +
                ")\n" +
                "\n" +
                ", FAT_TOTAL AS (\n" +
                "    SELECT (SUM(VALOR_1464) / (EXTRACT(MONTH FROM SYSDATE) - 1))\n" +
                "    FROM OPT_NOTASFATURADAS_VIEW_MAT\n" +
                "    WHERE CODFILIAL IN (6,7,8)\n" +
                "          AND EXTRACT(YEAR FROM DTFAT) = EXTRACT(YEAR FROM SYSDATE)\n" +
                "          AND EXTRACT(MONTH FROM DTFAT) < EXTRACT(MONTH FROM SYSDATE)\n" +
                ")\n" +
                "\n" +
                ", META AS (\n" +
                "    SELECT SUPERVISOR, PRACA, SUM(META) META\n" +
                "    FROM OPT_METAS\n" +
                "    WHERE ANO = EXTRACT(YEAR FROM SYSDATE)\n" +
                "          AND MES < EXTRACT(MONTH FROM SYSDATE)\n" +
                "    GROUP BY SUPERVISOR, PRACA\n" +
                "    ORDER BY SUPERVISOR, PRACA\n" +
                ")\n" +
                "\n" +
                ", CONSULTA AS (\n" +
                "    SELECT META.PRACA PRACA\n" +
                "           , MEDIA_ATUAL.MEDIA MEDIA_ATUAL\n" +
                "           , ROUND(MEDIA_ATUAL.MEDIA * 100 / (SELECT * FROM FAT_TOTAL),2) PERCENT_PARTICIP\n" +
                "           , (SELECT MMH.MEDIA FROM MAIOR_MEDIA_HISTORICA MMH \n" +
                "              WHERE MMH.PRACA = META.PRACA\n" +
                "              ORDER BY MMH.MEDIA DESC\n" +
                "              FETCH FIRST 1 ROWS ONLY) MAIOR_MEDIA_HISTORICA\n" +
                "           , MEDIA_ATUAL.FAT_ATUAL * 100 / META.META PERCENT_META_ATING\n" +
                "    FROM META\n" +
                "         LEFT JOIN MEDIA_ATUAL ON MEDIA_ATUAL.PRACA = META.PRACA\n" +
                "    WHERE MEDIA_ATUAL.MEDIA IS NOT NULL\n" +
                ")\n" +
                "\n" +
                "SELECT PRACA\n" +
                "       , MEDIA_ATUAL\n" +
                "       , PERCENT_PARTICIP\n" +
                "       , MAIOR_MEDIA_HISTORICA\n" +
                "       , ROUND(((MEDIA_ATUAL - MAIOR_MEDIA_HISTORICA) / MAIOR_MEDIA_HISTORICA) * 100, 2) AS PERC_CRESCIMENTO\n" +
                "       , ROUND(PERCENT_META_ATING,2) PERC_META_ATING\n" +
                "       , CASE\n" +
                "          WHEN MEDIA_ATUAL < MAIOR_MEDIA_HISTORICA \n" +
                "            THEN ROUND((MAIOR_MEDIA_HISTORICA - MEDIA_ATUAL) / MEDIA_ATUAL * 100,2)\n" +
                "          ELSE 0\n" +
                "         END PERC_POTENCIAL_CRESCIMENTO\n" +
                "FROM CONSULTA";

        List<Object[]> resultados = manager
                .createNativeQuery(sql)
                .getResultList();


        return resultados.stream()
                .map(linha -> new AnaliseGeral(
                        (String) linha[0],
                        (BigDecimal) linha[1],
                        (BigDecimal) linha[2],
                        (BigDecimal) linha[3],
                        (BigDecimal) linha[4],
                        (BigDecimal) linha[5],
                        (BigDecimal) linha[6]
                ))
                .toList();
    }
}
