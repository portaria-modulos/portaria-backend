package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "BLOCO_ARMARIO")
@AllArgsConstructor
@NoArgsConstructor
public class BlocoChaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer numero;
    private boolean ativo;
    @ManyToOne
    @JoinColumn(name = "armario_id")
    private Armario armario;
    private String UsuarioOcupacao;
    private Long  UsuarioOcupacaoId;
    private boolean disponivel;
    @Enumerated(EnumType.STRING)
    private StatusArmario status;
    @Column(length = 1000)
    private String descricaoProblema;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Armario getArmario() {
        return armario;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public StatusArmario getStatus() {
        return status;
    }

    public void setStatus(StatusArmario status) {
        this.status = status;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
    }

    public String getUsuarioOcupacao() {
        return UsuarioOcupacao;
    }

    public Long getUsuarioOcupacaoId() {
        return UsuarioOcupacaoId;
    }

    public void setUsuarioOcupacao(String usuarioOcupacao) {
        UsuarioOcupacao = usuarioOcupacao;
    }

    public void setUsuarioOcupacaoId(Long usuarioOcupacaoId) {
        UsuarioOcupacaoId = usuarioOcupacaoId;
    }
}

