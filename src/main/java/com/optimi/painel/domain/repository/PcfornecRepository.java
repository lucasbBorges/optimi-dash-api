package com.optimi.painel.domain.repository;

import com.optimi.painel.domain.model.main.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PcfornecRepository extends JpaRepository<Fornecedor, Long> {
}
