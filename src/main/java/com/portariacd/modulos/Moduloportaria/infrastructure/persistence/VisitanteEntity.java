package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.RegistroPortariaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "visitante")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VisitanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nomeCompleto;
    private String numeroTelefone;
    private String placaCarro;
    private String imagem;
    private String ocupacao;
    private Integer filial;
    private LocalDateTime dataCriacao;
    private  String tipoPessoa;
    private Boolean ativo;
    private Boolean acessoGlobal = false;
    private LocalDate dataRestritoAcesso;
    private Boolean bloqueioAcesso = false;
    @ManyToOne
    private Recorrencia recorrencia;

    public VisitanteEntity(RegistroPortariaDTO request, String image) {
        this.nomeCompleto = request.nomeCompleto();
        if(request.filial()!=null){
            this.filial = request.filial();
        }
        this.ocupacao = request.tipPessoa();
        this.numeroTelefone = request.numeroTelefone();
        this.imagem = image;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.placaCarro = request.placaVeiculo().toUpperCase();
        this.tipoPessoa = request.tipPessoa();
    }

    public VisitanteEntity(RegistroPortariaRequestDTO request, String nameImagem) {
        this.nomeCompleto = request.getNomeCompleto();
        if(request.getFilial()!=null){
            this.filial = request.getFilial();
        }
        this.ocupacao = request.getOcupacaoLiberada();
        this.numeroTelefone = request.getNumeroTelefone();
        this.imagem = nameImagem;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.placaCarro = request.getPlacaVeiculo().toUpperCase();
        this.tipoPessoa = request.getTipPessoa();
    }
}
