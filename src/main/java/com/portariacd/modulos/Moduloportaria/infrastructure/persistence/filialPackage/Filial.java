package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "filias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Filial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer numeroFilial;
    private String nome;
    private Boolean ativo;
    public Filial(String nome,Integer numero){
        this.nome = nome;
        this.numeroFilial = numero;
        this.ativo = true;
    }

}
