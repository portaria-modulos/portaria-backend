package com.portariacd.modulos.Moduloportaria.domain.models.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlteraSenhaDTO(
        @NotNull
        Long usuarioId,
        @NotBlank
        String novaSenha,
        @NotBlank
        String reperteSenha,
        @NotBlank
        String senhaAntiga) {
}
