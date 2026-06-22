package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.SistemaAcesso;

public record SistemaAcessoUsuarioDTO(
        Long id,
String titulo,String subtitulo,String permission_name,String router
) {

    public SistemaAcessoUsuarioDTO(SistemaAcesso modulo) {
        this(modulo.getId(),modulo.getTitulo(),modulo.getSubtitulo(),modulo.getPermission_name(),modulo.getRouter());
    }
}
