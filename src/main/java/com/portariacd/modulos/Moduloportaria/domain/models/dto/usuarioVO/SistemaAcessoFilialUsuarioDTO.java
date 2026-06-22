package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.Filial;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.UsuarioFilialEntity;

public record SistemaAcessoFilialUsuarioDTO(
        Long id,
String nome,Integer filial
) {

    public SistemaAcessoFilialUsuarioDTO(Filial filial) {
        this(filial.getId(),filial.getNome(),filial.getNumeroFilial());
    }

    public SistemaAcessoFilialUsuarioDTO(UsuarioFilialEntity usuarioFilialEntity) {
        this(usuarioFilialEntity.getFilial().getId(),usuarioFilialEntity.getFilial().getNome(),usuarioFilialEntity.getFilial().getNumeroFilial());
    }
}
