package com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroFilialDTO(@NotBlank String nome, @NotNull Integer numeroFilial){}
