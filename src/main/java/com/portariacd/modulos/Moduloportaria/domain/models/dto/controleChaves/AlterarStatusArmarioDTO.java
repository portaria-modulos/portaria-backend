package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves;

import jakarta.validation.constraints.NotBlank;

public record AlterarStatusArmarioDTO(@NotBlank String status, String descricaoProblema) {
}
