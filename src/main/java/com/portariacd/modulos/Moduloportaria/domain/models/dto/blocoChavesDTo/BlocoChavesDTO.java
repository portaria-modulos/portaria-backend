package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BlocoChavesDTO(@NotNull Long amarioId,@NotNull Integer quantidade) {



    public BlocoChavesDTO(BlocoChaves e) {
        this(e.getId(),e.getNumero());
    }
}
