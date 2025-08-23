package com.optimi.painel.domain.repository.impl;

import com.optimi.painel.controller.model.dto.meta.MetaDto;
import com.optimi.painel.controller.model.dto.meta.MetaRequest;
import com.optimi.painel.domain.model.main.Praca;
import com.optimi.painel.domain.repository.MetasDashRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetasDashRepositoryImpl implements MetasDashRepositoryCustom {

    @PersistenceContext
    EntityManager manager;

    @Override
    public List<Praca> listarPracas() {
        String sql = "SELECT DISTINCT PRACA\n" +
                "FROM OPT_LSTRCA\n" +
                "WHERE ATIVO = 'S'\n" +
                "      AND PRACA NOT LIKE '%COORD%'\n" +
                "      AND SUPERVISOR NOT LIKE '%TELE%'\n" +
                "      AND PRACA NOT LIKE '%.%'\n" +
                "      AND PRACA NOT LIKE '%NUTRI%'\n" +
                "      AND PRACA NOT LIKE '%HILL%'\n" +
                "      AND PRACA NOT LIKE '%AVERT%'\n" +
                "      AND PRACA NOT LIKE '%AVRT%'\n" +
                "ORDER BY 1";

        List<String> resultados = (List<String>) manager
                .createNativeQuery(sql)
                .getResultList();

        return resultados.stream()
                .map(Praca::new)
                .toList();
    }

    @Override
    public Slice<MetaDto> listarMetaMesCorrente(Pageable pageable, MetaRequest metaRequest) {
        String select = """
            SELECT META.ID
                 , META.ANO
                 , META.MES
                 , META.SUPERVISOR
                 , META.PRACA
                 , META.CODFORNEC
                 , PCFORNEC.FANTASIA
                 , META.META
            """;

        String fromWhere = """
                    FROM OPT_METAS META
                    LEFT JOIN PCFORNEC ON META.CODFORNEC = PCFORNEC.CODFORNEC
                    WHERE META.ANO = NVL(:ano,  EXTRACT(YEAR  FROM SYSDATE))
                      AND META.MES = NVL(:mes,  EXTRACT(MONTH FROM SYSDATE))
                      AND (:supervisor IS NULL OR META.SUPERVISOR = :supervisor)
                      AND (:praca IS NULL OR META.PRACA = :praca)
                      AND META.META > 0
                """;

        String orderBy = " ORDER BY META.SUPERVISOR, META.PRACA, PCFORNEC.FANTASIA, META.ID";

        var q = manager.createNativeQuery(select + fromWhere + orderBy);

        q.setParameter("ano", metaRequest.ano());
        q.setParameter("mes", metaRequest.mes());
        q.setParameter("supervisor", blankToNull(metaRequest.supervisor()));
        q.setParameter("praca", blankToNull(metaRequest.praca()));

        q.setFirstResult((int) pageable.getOffset());
        q.setMaxResults(pageable.getPageSize() + 1);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        List<MetaDto> mapped = rows.stream()
                .map(l -> new MetaDto(
                        asLong(l[0]),       // ID
                        asInt(l[1]),        // ANO
                        asInt(l[2]),        // MES
                        asString(l[3]),     // SUPERVISOR
                        asString(l[4]),     // PRACA
                        asLong(l[5]),       // CODFORNEC
                        asBigDecimal(l[7]), // META
                        asString(l[6])      // FANTASIA
                ))
                .toList();

        return toSlicePlusOne(mapped, pageable);
    }

    private static String blankToNull(String s){ return (s == null || s.isBlank()) ? null : s; }

    private static Long asLong(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.longValue();
        return Long.valueOf(o.toString());
    }

    private static Integer asInt(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.intValue();
        return Integer.valueOf(o.toString());
    }

    private static java.math.BigDecimal asBigDecimal(Object o) {
        if (o == null) return null;
        if (o instanceof java.math.BigDecimal bd) return bd;
        if (o instanceof Number n) return new java.math.BigDecimal(n.toString());
        return new java.math.BigDecimal(o.toString());
    }

    private static String asString(Object o) {
        return (o == null) ? null : o.toString();
    }

    private <T> Slice<T> toSlicePlusOne(List<T> rows, Pageable pageable) {
        boolean hasNext = rows.size() > pageable.getPageSize();
        List<T> content = hasNext ? rows.subList(0, pageable.getPageSize()) : rows;
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
