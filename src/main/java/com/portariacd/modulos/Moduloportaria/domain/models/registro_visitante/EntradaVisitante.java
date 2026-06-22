package com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;

import java.time.LocalDateTime;

public class EntradaVisitante {
    private LocalDateTime dataEntrada;
    private String nomeFiscal;
    private Long fiscalEntradaId;
    private String imagem;
    private Integer filial;


    public EntradaVisitante(EntradaVisitanteEntity entradaVisitante) {
        this.dataEntrada = entradaVisitante.getDataEntrada();
        this.nomeFiscal = entradaVisitante.getNomeFiscal();
        this.fiscalEntradaId = entradaVisitante.getFiscalEntradaId();
        this.imagem = entradaVisitante.getImagem();
        this.filial = entradaVisitante.getFilial();

    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Long getFiscalEntradaId() {
        return fiscalEntradaId;
    }

    public void setFiscalEntradaId(Long fiscalEntradaId) {
        this.fiscalEntradaId = fiscalEntradaId;
    }

    public String getNomeFiscal() {
        return nomeFiscal;
    }

    public void setNomeFiscal(String nomeFiscal) {
        this.nomeFiscal = nomeFiscal;
    }

    public Integer getFilial() {
        return filial;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }
}
