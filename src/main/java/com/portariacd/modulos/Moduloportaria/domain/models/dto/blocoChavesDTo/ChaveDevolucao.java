package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import jakarta.validation.constraints.NotNull;

public record ChaveDevolucao(@NotNull  Integer chave,@NotNull Long arm) {
}
