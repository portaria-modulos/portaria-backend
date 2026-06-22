package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;

public record StatusAtualizadoDTO(
        long id,
        String nomeCompleto,
        String placaVeiculo,
        String status,
        String msg
) {
    public StatusAtualizadoDTO(RegistroVisitantePortariaEntity resp, String statusAlterado) {
    this(resp.getId()
            ,resp.getNomeCompleto()
            ,resp.getPlacaVeiculo(),
            resp.getStatus().name(),
            statusAlterado
    );
    }
}
