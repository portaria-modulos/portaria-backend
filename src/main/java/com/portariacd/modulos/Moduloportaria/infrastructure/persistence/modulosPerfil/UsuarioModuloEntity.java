package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.Filial;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.SistemaAcesso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_modulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModuloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UsuarioEntity usuario;
    @ManyToOne()
    private SistemaAcesso modulo;
    @ManyToOne()
    private Filial filial;
    private boolean ativo;
    public UsuarioModuloEntity(UsuarioEntity usuario,SistemaAcesso acesso){
        this.usuario = usuario;
        this.modulo = acesso;
        this.ativo = true;
    }

}
