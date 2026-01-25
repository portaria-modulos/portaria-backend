package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial;

import jakarta.validation.constraints.NotNull;

public record FiliaisModulesDTO(@NotNull Long permissionId, @NotNull Boolean ativo) {
}
