package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;
import com.portariacd.modulos.Moduloportaria.domain.models.auth.Perfil;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.PermissionEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.UsuarioModuloEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Entity
@Table(name = "perfil")
@AllArgsConstructor
@NoArgsConstructor
public class PerfilEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String nome;
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL)
    List<UsuarioEntity> usuario;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<PermissionEntity> permissoes;
    public PerfilEntity(Perfil pefil) {
        this.nome = pefil.getNome();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<UsuarioEntity> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<UsuarioEntity> usuario) {
        this.usuario = usuario;
    }

    public Set<PermissionEntity> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<PermissionEntity> permissoes) {
        this.permissoes = permissoes;
    }
}
