package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.AtualizaRegistro;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.RegistroPortariaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_portaria")
@AllArgsConstructor
@NoArgsConstructor
public class RegistroVisitantePortariaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nomeCompleto;
    @ManyToOne
    private VisitanteEntity visitante;
    private String placaVeiculo;
    private String ocupacaoLiberada;
    private LocalDateTime dataCriacao;
    @Lob
    @Column(name = "descricao",columnDefinition = "TEXT")
    @Basic(fetch = FetchType.EAGER)
    private String descricao;
    @OneToOne(cascade = CascadeType.ALL)
    private SaidaVisitanteEntity saidaVisitante;
    @OneToOne(cascade = CascadeType.ALL)
    private EntradaVisitanteEntity entradaVisitante;
    @Enumerated(EnumType.STRING)
    private StatusPortaria status;
    @ManyToOne
    private UsuarioEntity criador;
    private Integer filialSolicitado;
    private Boolean ativo;
    private String protocolo;
    private String bloco;
    private Boolean prioridadeAtrasoAtivo;
    private String prioridadeAviso;
    private String prioridadeAtraso;
    public RegistroVisitantePortariaEntity(RegistroPortariaDTO req, VisitanteEntity visitante, UsuarioEntity usuario) {
      this.nomeCompleto = req.nomeCompleto();
      this.visitante = visitante;
      this.placaVeiculo = req.placaVeiculo().toUpperCase();
      this.dataCriacao = LocalDateTime.now();
      this.bloco = req.bloco();
      this.descricao = req.descricao();
      this.ocupacaoLiberada = req.ocupacaoLiberada();
      this.criador = usuario;
      this.filialSolicitado = req.filialSolicitado();
      this.ativo = true;
    }

    public RegistroVisitantePortariaEntity(RegistroPortariaRequestDTO req, VisitanteEntity visitanteRequest, UsuarioEntity usuario) {
        this.nomeCompleto = req.getNomeCompleto();
        this.visitante = visitanteRequest;
        this.placaVeiculo = req.getPlacaVeiculo().toUpperCase();
        this.dataCriacao = LocalDateTime.now();
        this.bloco = req.getBloco();
        this.descricao = req.getDescricao();
        this.ocupacaoLiberada = req.getOcupacaoLiberada();
        this.criador = usuario;
        this.filialSolicitado = req.getFilialSolicitado();
        this.ativo = true;
    }

    public void atualizarEntrada(AtualizaRegistro update) {
        this.placaVeiculo = update.placaVeiculo();
        this.nomeCompleto = update.nomeCompleto();
        this.bloco = update.bloco();
        this.ocupacaoLiberada = update.ocupacaoLiberada();
        if (update.filialSolicitado() != null) {
            this.filialSolicitado = update.filialSolicitado();
        }
    }
//    @PrePersist
//    public void gerarId() {
//
//        if (id == null) {
//            long timestamp = System.currentTimeMillis();
//            int random = new Random().nextInt(99999);
//            this.id = timestamp + "-" + random;
//        }
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public VisitanteEntity getVisitante() {
        return visitante;
    }

    public void setVisitante(VisitanteEntity visitante) {
        this.visitante = visitante;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getOcupacaoLiberada() {
        return ocupacaoLiberada;
    }

    public void setOcupacaoLiberada(String ocupacaoLiberada) {
        this.ocupacaoLiberada = ocupacaoLiberada;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public SaidaVisitanteEntity getSaidaVisitante() {
        return saidaVisitante;
    }

    public void setSaidaVisitante(SaidaVisitanteEntity saidaVisitante) {
        this.saidaVisitante = saidaVisitante;
    }

    public EntradaVisitanteEntity getEntradaVisitante() {
        return entradaVisitante;
    }

    public void setEntradaVisitante(EntradaVisitanteEntity entradaVisitante) {
        this.entradaVisitante = entradaVisitante;
    }

    public StatusPortaria getStatus() {
        return status;
    }

    public void setStatus(StatusPortaria status) {
        this.status = status;
    }

    public UsuarioEntity getCriador() {
        return criador;
    }

    public void setCriador(UsuarioEntity criador) {
        this.criador = criador;
    }

    public Integer getFilialSolicitado() {
        return filialSolicitado;
    }

    public void setFilialSolicitado(Integer filialSolicitado) {
        this.filialSolicitado = filialSolicitado;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public Boolean getPrioridadeAtrasoAtivo() {
        return prioridadeAtrasoAtivo;
    }

    public void setPrioridadeAtrasoAtivo(Boolean prioridadeAtrasoAtivo) {
        this.prioridadeAtrasoAtivo = prioridadeAtrasoAtivo;
    }

    public String getPrioridadeAviso() {
        return prioridadeAviso;
    }

    public void setPrioridadeAviso(String prioridadeAviso) {
        this.prioridadeAviso = prioridadeAviso;
    }

    public String getPrioridadeAtraso() {
        return prioridadeAtraso;
    }

    public void setPrioridadeAtraso(String prioridadeAtraso) {
        this.prioridadeAtraso = prioridadeAtraso;
    }
}

