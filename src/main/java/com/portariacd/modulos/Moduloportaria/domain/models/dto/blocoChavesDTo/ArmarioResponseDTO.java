package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArmarioResponseDTO(Long id,@NotNull  Long filial, @NotBlank String tipo, List<BlocoChavesResponseDTO> bloco) {

    public ArmarioResponseDTO(Armario e) {
        this(e.getId(),e.getFilial(),e.getTipo().name(),e.getBlocoChaves().stream().map(BlocoChavesResponseDTO::new).toList());
    }
}


