package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.FiliaisModulesDTO;

import java.util.List;

public interface FiliaisModulosGatewayRepository {
    void addPermission(List<FiliaisModulesDTO> pemission,Long usuarioId);
    List<SistemaAcessoFilialUsuarioDTO> listaPermission();
    List<SistemaAcessoFilialUsuarioDTO> listaFiliaisUsuario(Long id);
}
