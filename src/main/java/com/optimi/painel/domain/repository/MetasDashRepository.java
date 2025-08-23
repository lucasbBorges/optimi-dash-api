package com.optimi.painel.domain.repository;

import com.optimi.painel.domain.model.main.MetaTabela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetasDashRepository extends JpaRepository<MetaTabela, Long>, MetasDashRepositoryCustom {
}
