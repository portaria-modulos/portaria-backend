package com.portariacd.modulos.Moduloportaria.controllers.BlocoControler;

import jakarta.validation.constraints.NotBlank;

public record BiometriaImagem(@NotBlank String base64) {
}
