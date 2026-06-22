package com.portariacd.modulos.Moduloportaria.domain.models.dto;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArmarioDTO(@NotNull  Long filial,@NotBlank String tipo) {

    public ArmarioDTO(Armario e) {
        this(e.getFilial(),e.getTipo().name());
    }
}


