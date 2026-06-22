package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.Usuario;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.UsuarioFilialEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.UsuarioModuloEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String avatar;
    private String email;
    private String password;
    private LocalDateTime dataCriacao;
    private String ocupacaoOperacional;
    private int filial;
    @ManyToOne
    private PerfilEntity perfil;
    @OneToMany(mappedBy = "criador",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RegistroVisitantePortariaEntity> registros;
    private Boolean ativo;
    @OneToMany(mappedBy = "usuario")
    @Where(clause = "ativo = true")
    private Set<UsuarioModuloEntity> modulos;

    @OneToMany(mappedBy = "usuario")
    @Where(clause = "ativo = true")
    private Set<UsuarioFilialEntity> filiais;
    @Column(name = "current_session")
    private String currentSession;
    @Column(name = "session_expires_at")
    private LocalDateTime sessionExpiresAt;

    @Column(name = "session_device")
    private String sessionDevice; // desktop, mobile, tablet...
    @Column(name = "session_last_login")
    private LocalDateTime sessionLastLogin;
    @Column(name = "session_ip")
    private String sessionIp;


    private String ultimoNavegador;

    public UsuarioEntity(Usuario usuario,PerfilEntity perfil) {
        this.nome  = usuario.getNome();
        this.email = usuario.getEmail().toLowerCase();
        this.ocupacaoOperacional = usuario.getOcupacaoOperacional();
        this.ativo =usuario.getAtivo();
        this.filial = usuario.getFilial();
        this.dataCriacao = usuario.getCreateDate();
        this.password = usuario.getPassword();
        if(perfil!=null){
            this.perfil = perfil;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> rolesPermition = Set.of(
                "ROLE_ADMIN",
                "FISCAL",
                "AUTORIZADOR"
        );
        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
           if(perfil.permissoes!=null) {
               if (rolesPermition.contains(perfil.getNome())) {
                   authorities.add(new SimpleGrantedAuthority(perfil.getNome()));
               }
               perfil.getPermissoes().forEach(permitionEntity -> {
                   authorities.add(new SimpleGrantedAuthority(permitionEntity.getName()));
               });
           }
        return authorities;
    }
    public String criptografiaDeSenha(
            String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public void atualizaSenha(String password){
      this.password =  criptografiaDeSenha(password);
    }


    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getOcupacaoOperacional() {
        return ocupacaoOperacional;
    }

    public void setOcupacaoOperacional(String ocupacaoOperacional) {
        this.ocupacaoOperacional = ocupacaoOperacional;
    }

    public int getFilial() {
        return filial;
    }

    public void setFilial(int filial) {
        this.filial = filial;
    }

    public PerfilEntity getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEntity perfil) {
        this.perfil = perfil;
    }

    public List<RegistroVisitantePortariaEntity> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroVisitantePortariaEntity> registros) {
        this.registros = registros;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<UsuarioModuloEntity> getModulos() {
        return modulos;
    }

    public void setModulos(Set<UsuarioModuloEntity> modulos) {
        this.modulos = modulos;
    }

    public Set<UsuarioFilialEntity> getFiliais() {
        return filiais;
    }

    public void setFiliais(Set<UsuarioFilialEntity> filiais) {
        this.filiais = filiais;
    }

    public String getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(String currentSession) {
        this.currentSession = currentSession;
    }

    public LocalDateTime getSessionExpiresAt() {
        return sessionExpiresAt;
    }

    public void setSessionExpiresAt(LocalDateTime sessionExpiresAt) {
        this.sessionExpiresAt = sessionExpiresAt;
    }

    public String getSessionDevice() {
        return sessionDevice;
    }

    public void setSessionDevice(String sessionDevice) {
        this.sessionDevice = sessionDevice;
    }

    public LocalDateTime getSessionLastLogin() {
        return sessionLastLogin;
    }

    public void setSessionLastLogin(LocalDateTime sessionLastLogin) {
        this.sessionLastLogin = sessionLastLogin;
    }

    public String getSessionIp() {
        return sessionIp;
    }

    public void setSessionIp(String sessionIp) {
        this.sessionIp = sessionIp;
    }

    public String getUltimoNavegador() {
        return ultimoNavegador;
    }

    public void setUltimoNavegador(String ultimoNavegador) {
        this.ultimoNavegador = ultimoNavegador;
    }
}
