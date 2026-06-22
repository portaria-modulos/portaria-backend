package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.SistemaAcessoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
@Entity
@Table(name = "module_permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SistemaAcesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String subtitulo;
    private String router;
    private Boolean ativo;
    private String permission_name;
    public SistemaAcesso(SistemaAcessoDTO sistemaAcesso){
    this.titulo = sistemaAcesso.titulo();
    this.subtitulo = sistemaAcesso.subtitulo();
    this.router = sistemaAcesso.router();
    this.ativo =sistemaAcesso.ativo();
    this.permission_name = sistemaAcesso.permission_name();
    }
}
