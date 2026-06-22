package com.portariacd.modulos.Moduloportaria.domain.models.history;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;

import java.time.LocalDateTime;
public class HistoryEntrada {
    private long id;
    private String nomeCompleto;
    private VisitanteEntity visitante;
    private long registroPortariaId;
    private String placaVeiculo;
    private LocalDateTime dataCriacao;
    private String descricao;
    private StatusPortaria status;
    private UsuarioEntity criador;
    private Integer filialSolicitado;
    private Boolean ativo;
    private String protocolo;
    private String bloco;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Long ficalIdEntrada;
    private Long ficalIdSaida;
    private String nomeFiscaEntrada;
    private String nomeFiscaSaida;
    private String imagemEntrada;
    private String imagemSaida;

    public HistoryEntrada(RegistroVisitantePortariaEntity r) {
        this.nomeCompleto = r.getNomeCompleto();
        this.visitante = r.getVisitante();
        this.registroPortariaId = r.getId();
        this.placaVeiculo = r.getPlacaVeiculo();
        this.dataCriacao = r.getDataCriacao();
        this.descricao = r.getDescricao();
        this.status = r.getStatus();
        this.criador = r.getCriador();
        this.filialSolicitado = r.getFilialSolicitado();
        this.ativo = r.getAtivo();
        this.protocolo = r.getProtocolo();
        this.bloco = r.getBloco();
    }

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
    public String getNomeFiscaEntrada() {
        return nomeFiscaEntrada;
    }

    public void setNomeFiscaEntrada(String nomeFiscaEntrada) {
        this.nomeFiscaEntrada = nomeFiscaEntrada;
    }

    public String getNomeFiscaSaida() {
        return nomeFiscaSaida;
    }

    public void setNomeFiscaSaida(String nomeFiscaSaida) {
        this.nomeFiscaSaida = nomeFiscaSaida;
    }

    public String getImagemEntrada() {
        return imagemEntrada;
    }

    public void setImagemEntrada(String imagemEntrada) {
        this.imagemEntrada = imagemEntrada;
    }

    public String getImagemSaida() {
        return imagemSaida;
    }

    public void setImagemSaida(String imagemSaida) {
        this.imagemSaida = imagemSaida;
    }

    public long getRegistroPortariaId() {
        return registroPortariaId;
    }

    public void setRegistroPortariaId(long registroPortariaId) {
        this.registroPortariaId = registroPortariaId;
    }

    public Long getFicalIdEntrada() {
        return ficalIdEntrada;
    }

    public void setFicalIdEntrada(Long ficalIdEntrada) {
        this.ficalIdEntrada = ficalIdEntrada;
    }

    public Long getFicalIdSaida() {
        return ficalIdSaida;
    }

    public void setFicalIdSaida(Long ficalIdSaida) {
        this.ficalIdSaida = ficalIdSaida;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

}
