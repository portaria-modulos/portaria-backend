package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;

import java.util.List;

public interface ModuloGatewayRepository {
    void addPermission(List<ModuleDTO> pemission, FilialDTOMod mod, Long usuarioId);
    List<SistemaAcessoUsuarioDTO> listaPermission();
}
