package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;
import jakarta.validation.Valid;

import java.util.List;

public record JsonModuleFilial(@Valid List<FiliaisModulesDTO> lista) {
}
