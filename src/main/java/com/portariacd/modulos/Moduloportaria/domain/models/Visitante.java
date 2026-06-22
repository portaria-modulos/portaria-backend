package com.portariacd.modulos.Moduloportaria.domain.models;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;

import java.time.LocalDateTime;
public class Visitante {
    private Long id;
    private String nomeCompleto;
    private String imagem;
    private String ocupacao;
    private Boolean ativo;
    //opcional
    private Integer filial;
    private String numeroTelefone;
    private LocalDateTime dataCriacao;
//    private StatusTipoDeAcesso tipoAcesso;
    private  String tipoPessoa;
    RequestRecorrenciaDTO recorrencia;
    private String placaVeiculo;

    public Visitante(VisitanteEntity visitante) {
        this.id = visitante.getId();
        this.nomeCompleto = visitante.getNomeCompleto();
        this.dataCriacao = visitante.getDataCriacao();
        this.imagem = visitante.getImagem();
//        this.tipoAcesso = visitante.getTipoAcesso();
        if(visitante.getFilial()!=null){
            this.filial = visitante.getFilial();
        }
        this.tipoPessoa = visitante.getTipoPessoa();
        this.numeroTelefone = visitante.getNumeroTelefone();
        if(visitante.getRecorrencia()!=null){
            recorrencia = new RequestRecorrenciaDTO(visitante.getRecorrencia());
        }
        this.ativo = visitante.getAtivo();
        this.placaVeiculo = visitante.getPlacaCarro();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }


    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getFilial() {
        return filial;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }



    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }


    public RequestRecorrenciaDTO getRecorrencia() {
        return recorrencia;
    }

    public void setRecorrencia(RequestRecorrenciaDTO recorrencia) {
        this.recorrencia = recorrencia;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }
}
