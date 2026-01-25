package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.SistemaAcesso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_filiais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFilialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UsuarioEntity usuario;
    @ManyToOne()
    private Filial filial;
    private boolean ativo;
    public UsuarioFilialEntity(UsuarioEntity usuario, Filial filial){
        this.usuario = usuario;
        this.filial = filial;
        this.ativo = true;
    }
}
