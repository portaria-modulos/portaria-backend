package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.SistemaAcessoDTO;

import java.util.List;

public record UsuarioModuloDto(List<SistemaAcessoDTO> acess){
}
