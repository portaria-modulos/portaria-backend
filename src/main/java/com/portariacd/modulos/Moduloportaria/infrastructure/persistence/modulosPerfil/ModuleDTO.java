package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil;

import jakarta.validation.constraints.NotNull;

public record ModuleDTO(@NotNull Long permissionId,@NotNull Boolean ativo) {
}
