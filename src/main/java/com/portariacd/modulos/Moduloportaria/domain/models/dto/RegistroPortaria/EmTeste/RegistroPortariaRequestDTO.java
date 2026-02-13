package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste;

import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegistroPortariaRequestDTO extends CadastroTypeFacture {
        @NotBlank
        String nomeCompleto;
        @NotBlank
        String numeroTelefone;
        @NotBlank
        String placaVeiculo;
        @NotBlank
        String tipoAcesso;
        @NotNull
        Integer filial;
        @NotBlank
        String tipPessoa;
        @NotBlank
        String ocupacaoLiberada;
        String descricao;
        @NotNull
        Long criadorId;
        @NotBlank
        String bloco;
        String globalAtivo;
        String dataAcesso;
        @NotNull
        Integer filialSolicitado;
         public RegistroPortariaRequestDTO(){}
        public RegistroPortariaRequestDTO(RegistroVisitantePortariaEntity resposta) {
              this.nomeCompleto =  resposta.getNomeCompleto();
                   this.numeroTelefone= resposta.getVisitante().getNumeroTelefone();
                      this.placaVeiculo= resposta.getPlacaVeiculo();
                       this.tipoAcesso = resposta.getVisitante().getRecorrencia()!=null?resposta.getVisitante().getRecorrencia().getNome():null;
                       this.filial= resposta.getVisitante().getFilial();
                       this.tipPessoa = resposta.getVisitante().getTipoPessoa();
                       this.ocupacaoLiberada = resposta.getOcupacaoLiberada();
                       this.descricao= resposta.getDescricao();
                       this.criadorId = resposta.getCriador().getId();
                       this.bloco = resposta.getBloco();
                       this.filialSolicitado = resposta.getFilialSolicitado();
        }

        public RegistroPortariaRequestDTO(AtualizaRegistroPortariaDTO request, VisitanteEntity v) {
                this.numeroTelefone= v.getNumeroTelefone();
                this.nomeCompleto =  v.getNomeCompleto();
                this.placaVeiculo = v.getPlacaCarro();
                this.ocupacaoLiberada = request.getOcupacaoLiberada();
                this.descricao= request.getDescricao();
                this.criadorId = request.criadorId;
                this.tipoAcesso = request.tipoAcesso;
                this.bloco = request.getBloco();
                this.globalAtivo = request.getGlobalAtivo();
                this.dataAcesso = request.dataAcesso;
        }


        public String getNomeCompleto() {
                return nomeCompleto;
        }

        public void setNomeCompleto(String nomeCompleto) {
                this.nomeCompleto = nomeCompleto;
        }

        public String getDataAcesso() {
                return dataAcesso;
        }

        public void setDataAcesso(String dataAcesso) {
                this.dataAcesso = dataAcesso;
        }

        public String getGlobalAtivo() {
                return globalAtivo;
        }

        public void setGlobalAtivo(String globalAtivo) {
                this.globalAtivo = globalAtivo;
        }

        public String getBloco() {
                return bloco;
        }

        public void setBloco(String bloco) {
                this.bloco = bloco;
        }

        public String getDescricao() {
                return descricao;
        }

        public void setDescricao(String descricao) {
                this.descricao = descricao;
        }

        public Long getCriadorId() {
                return criadorId;
        }

        public void setCriadorId(Long criadorId) {
                this.criadorId = criadorId;
        }

        public String getOcupacaoLiberada() {
                return ocupacaoLiberada;
        }

        public void setOcupacaoLiberada(String ocupacaoLiberada) {
                this.ocupacaoLiberada = ocupacaoLiberada;
        }

        public String getTipPessoa() {
                return tipPessoa;
        }

        public void setTipPessoa(String tipPessoa) {
                this.tipPessoa = tipPessoa;
        }

        public Integer getFilial() {
                return filial;
        }

        public void setFilial(Integer filial) {
                this.filial = filial;
        }

        public String getTipoAcesso() {
                return tipoAcesso;
        }

        public void setTipoAcesso(String tipoAcesso) {
                this.tipoAcesso = tipoAcesso;
        }

        public String getPlacaVeiculo() {
                return placaVeiculo;
        }

        public void setPlacaVeiculo(String placaVeiculo) {
                this.placaVeiculo = placaVeiculo;
        }

        public String getNumeroTelefone() {
                return numeroTelefone;
        }

        public void setNumeroTelefone(String numeroTelefone) {
                this.numeroTelefone = numeroTelefone;
        }

        public Integer getFilialSolicitado() {
                return filialSolicitado;
        }

        public void setFilialSolicitado(Integer filialSolicitado) {
                this.filialSolicitado = filialSolicitado;
        }
}