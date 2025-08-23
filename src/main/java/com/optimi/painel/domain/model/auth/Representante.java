package com.optimi.painel.domain.model.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="pcusuari")
@Getter
@Setter
public class Representante {
    @Id
    private Long codusur;
    private String nome;

}
