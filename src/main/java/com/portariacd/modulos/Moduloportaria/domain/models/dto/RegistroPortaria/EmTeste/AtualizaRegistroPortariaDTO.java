package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste;

import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AtualizaRegistroPortariaDTO extends CadastroTypeFacture {
        @NotNull
        Long visitanteId;
        @NotBlank
        String ocupacaoLiberada;
        String descricao;
        @NotNull
        Long criadorId;
        @NotBlank
        String bloco;
        String tipoAcesso;
        String globalAtivo;
        String dataAcesso;
        @NotNull
        Integer filialSolicitado;
        public Long getVisitanteId() {
                return visitanteId;
        }

        public void setVisitanteId(Long visitanteId) {
                this.visitanteId = visitanteId;
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

        public Long getCriadorId() {
                return criadorId;
        }

        public void setCriadorId(Long criadorId) {
                this.criadorId = criadorId;
        }

        public String getDescricao() {
                return descricao;
        }

        public void setDescricao(String descricao) {
                this.descricao = descricao;
        }

        public String getOcupacaoLiberada() {
                return ocupacaoLiberada;
        }

        public void setOcupacaoLiberada(String ocupacaoLiberada) {
                this.ocupacaoLiberada = ocupacaoLiberada;
        }

        public String getTipoAcesso() {
                return tipoAcesso;
        }

        public void setTipoAcesso(String tipoAcesso) {
                this.tipoAcesso = tipoAcesso;
        }

        public Integer getFilialSolicitado() {
                return filialSolicitado;
        }

        public void setFilialSolicitado(Integer filialSolicitado) {
                this.filialSolicitado = filialSolicitado;
        }
}