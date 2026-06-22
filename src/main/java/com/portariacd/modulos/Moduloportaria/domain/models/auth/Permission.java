package com.portariacd.modulos.Moduloportaria.domain.models.auth;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.PermissionEntity;


public class Permission {
    private long id;
    private String name;
    private boolean ativo;

    public Permission(PermissionEntity e) {
        this.id = e.getId();
        this.name = e.getName();
        this.ativo = e.isAtivo();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
