package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EntregaChavesDTO(
      @NotNull Long armarioId,
        @NotNull Integer numeroDaChave,
        @NotBlank String gmIDMatricula,
        @NotNull Long usuarioId
) {

}
