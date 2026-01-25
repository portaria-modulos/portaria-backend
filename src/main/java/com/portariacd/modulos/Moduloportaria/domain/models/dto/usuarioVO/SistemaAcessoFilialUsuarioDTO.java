package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.Filial;

public record SistemaAcessoFilialUsuarioDTO(
        Long id,
String nome,Integer filial
) {

    public SistemaAcessoFilialUsuarioDTO(Filial filial) {
        this(filial.getId(),filial.getNome(),filial.getNumeroFilial());
    }
}
