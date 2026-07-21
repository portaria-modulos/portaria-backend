package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;

public record UsuarioId(Long id,String nome,String matricula,Long filial) {

    public UsuarioId(UsuarioConsumerChaves usuario) {
        this(usuario.getId(),usuario.getNome(),usuario.getGmcoreId(),usuario.getFilial().longValue());
    }
}
