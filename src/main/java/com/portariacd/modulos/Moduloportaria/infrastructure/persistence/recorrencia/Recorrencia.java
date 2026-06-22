package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recorrencia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Boolean ativo;
    public Recorrencia(String nome){

        this.nome = nome;
        this.ativo = true;
    }

}
