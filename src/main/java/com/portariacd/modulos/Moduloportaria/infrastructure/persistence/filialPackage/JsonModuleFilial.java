package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage;

import jakarta.validation.Valid;

import java.util.List;

public record JsonModuleFilial(@Valid List<FiliaisModulesDTO> lista) {
}
