package com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ChaveDevolucao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DesvolucaoChaveUsuarioDto(@NotNull  Long usuarioId,String tipo, @NotBlank String maticulaGmId, ChaveDevolucao item) implements FactureMetodChave {
}
