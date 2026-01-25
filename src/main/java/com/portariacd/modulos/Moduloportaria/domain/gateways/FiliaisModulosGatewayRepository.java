package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.FiliaisModulesDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;

import java.util.List;

public interface FiliaisModulosGatewayRepository {
    void addPermission(List<FiliaisModulesDTO> pemission,Long usuarioId);
    List<SistemaAcessoFilialUsuarioDTO> listaPermission();
}
