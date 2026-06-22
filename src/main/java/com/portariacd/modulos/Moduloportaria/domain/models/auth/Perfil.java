package com.portariacd.modulos.Moduloportaria.domain.models.auth;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.PerfilEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class Perfil {
    long id;
    String nome;
    Usuario usuario;
    Set<Permission> permissoes;

    public Perfil(PerfilEntity perfil) {
        this.id = perfil.getId();
        this.nome = perfil.getNome();
        if(perfil.getPermissoes()!=null){
            this.permissoes = perfil.getPermissoes().stream().map(Permission::new).collect(Collectors.toSet());
        }
    }
    public Perfil() {

    }

    public long getId() {
        return id;
    }

    public void setId(long perfilId) {
        this.id = perfilId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Permission> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<Permission> permissoes) {
        this.permissoes = permissoes;
    }
}
