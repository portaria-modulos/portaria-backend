package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import jakarta.validation.constraints.NotNull;

public record BlocoChavesResponseDTO(Long amarioId, String tipo, Integer chave, String status,
                                     String descricaoProblema) {



    public BlocoChavesResponseDTO(BlocoChaves e) {
        this(e.getArmario().getId(), e.getArmario().getTipo().name(), e.getNumero(),
                e.getStatus().name(), e.getDescricaoProblema());
    }
}
