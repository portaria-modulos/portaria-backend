package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.tipoPessoa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tipo_pessoa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoPessoaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    public TipoPessoaEntity(String nome){
        this.nome = nome;
    }

    public void atualiza(String nome){
        this.nome = nome;
    }
}
