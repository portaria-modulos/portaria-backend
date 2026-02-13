package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroPortariaDTO(
        @NotBlank
        String nomeCompleto,
        @NotBlank
        String numeroTelefone,
        @NotBlank
        String placaVeiculo,
        @NotBlank
        String tipoAcesso,
        Integer filial,
        @NotBlank
        String tipPessoa,
        @NotBlank
        String ocupacaoLiberada,
        String descricao,
        @NotNull
        Long criadorId,
        @NotBlank
        String bloco,
        String globalAtivo,
        String dataAcesso,
        @NotNull
        Integer filialSolicitado

) {
    public RegistroPortariaDTO(RegistroVisitantePortariaEntity resposta) {
            this(
                    resposta.getNomeCompleto(),
            resposta.getVisitante().getNumeroTelefone(),
            resposta.getPlacaVeiculo(),
            resposta.getVisitante().getRecorrencia()!=null?resposta.getVisitante().getRecorrencia().getNome():null,
            resposta.getVisitante().getFilial(),
            resposta.getVisitante().getTipoPessoa(),
            resposta.getOcupacaoLiberada(),
            resposta.getDescricao(),
            resposta.getCriador().getId(),
            resposta.getBloco(),
                   null,
                    null,
                    resposta.getFilialSolicitado()
            );
    }

}
