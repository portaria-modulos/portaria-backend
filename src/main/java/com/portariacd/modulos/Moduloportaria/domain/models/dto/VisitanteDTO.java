package com.portariacd.modulos.Moduloportaria.domain.models.dto;

import com.portariacd.modulos.Moduloportaria.domain.models.Visitante;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;

import java.time.LocalDateTime;

public record VisitanteDTO(
         Long id,
         String nomeCompleto,
         String imagem,
         String ocupacao,
         String placaVeiculo,
        //opcional
         Integer filial,
         String numeroTelefone,
         LocalDateTime dataCriacao,
          String tipoPessoa,
         Boolean ativo,
         RequestRecorrenciaDTO recorrencia
) {
    public VisitanteDTO(Visitante v) {
        this(v.getId(),
                v.getNomeCompleto(),
                v.getImagem(),
                v.getOcupacao(),
                v.getPlacaVeiculo(),
                v.getFilial(),
                v.getNumeroTelefone(),
                v.getDataCriacao(),
                v.getTipoPessoa(),
                v.getAtivo(),
                v.getRecorrencia()
                );
    }

    public VisitanteDTO(VisitanteEntity v) {
        this(v.getId(),
                v.getNomeCompleto(),
                v.getImagem(),
                v.getOcupacao(),
                v.getPlacaCarro(),
                v.getFilial(),
                v.getNumeroTelefone(),
                v.getDataCriacao(),
                v.getTipoPessoa(),
                v.getAtivo(),
                v.getRecorrencia()!=null?new RequestRecorrenciaDTO(v.getRecorrencia()):null
        );
    }
}
