package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import jakarta.validation.Valid;

import java.util.List;

public record JsonModule(@Valid List<ModuleDTO> lista, FilialDTOMod mod) {
}
