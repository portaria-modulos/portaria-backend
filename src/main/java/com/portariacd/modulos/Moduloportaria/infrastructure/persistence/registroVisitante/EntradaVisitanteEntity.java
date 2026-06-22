package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "Entrada_visitante")
@AllArgsConstructor
@NoArgsConstructor
public class EntradaVisitanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dataEntrada;
    private String nomeFiscal;
    private Long fiscalEntradaId;
    private String imagem;
    private Integer filial;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getNomeFiscal() {
        return nomeFiscal;
    }

    public void setNomeFiscal(String nomeFiscal) {
        this.nomeFiscal = nomeFiscal;
    }

    public Long getFiscalEntradaId() {
        return fiscalEntradaId;
    }

    public void setFiscalEntradaId(Long fiscalEntradaId) {
        this.fiscalEntradaId = fiscalEntradaId;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getFilial() {
        return filial;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }
}
