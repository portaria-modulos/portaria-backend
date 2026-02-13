package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "bloco")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlocoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Long gmBlocoId;
    public  BlocoEntity(String nome){
        this.nome = nome;
    }


}
