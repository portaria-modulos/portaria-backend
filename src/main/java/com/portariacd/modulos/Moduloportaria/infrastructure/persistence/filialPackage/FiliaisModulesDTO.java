package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage;

import jakarta.validation.constraints.NotNull;

public record FiliaisModulesDTO(@NotNull Long permissionId, @NotNull Boolean ativo) {
}
