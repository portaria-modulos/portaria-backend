package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
public interface UsuarioProjection {
    Long getId();
    String getMatricula();
    String getGmcoreId();
    String getNome();
    String getSetor();
    Integer getFilial();
    Boolean getAtivo();
}
