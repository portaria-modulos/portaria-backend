package com.portariacd.modulos.Moduloportaria.domain.models.dto;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.VisitanteEntity;

public record VisitanteFiltroDTO(
         Long id,
         String nomeCompleto,
         String imagem,//opcional
         Integer filial
) {
    public VisitanteFiltroDTO(VisitanteEntity v) {
        this(v.getId(),
                v.getNomeCompleto(),
                v.getImagem(),
                v.getFilial()
        );
    }
}
