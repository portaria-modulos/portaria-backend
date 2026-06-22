package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="funcao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean ativo;
}
