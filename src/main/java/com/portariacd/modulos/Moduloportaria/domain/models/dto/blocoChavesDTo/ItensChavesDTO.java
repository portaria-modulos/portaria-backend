package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ItensChavesDTO(@NotEmpty  List<BlocoChavesDTO> itens) {
}
