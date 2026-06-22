package com.portariacd.modulos.Moduloportaria.domain.models.dto;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.SistemaAcesso;

public record SistemaAcessoDTO(
String titulo,String subtitulo,String router,Boolean ativo,String permission_name) {

    public SistemaAcessoDTO(SistemaAcesso modulo) {
        this(modulo.getTitulo(),modulo.getSubtitulo(),modulo.getRouter(),modulo.getAtivo(),modulo.getPermission_name());
    }
}
