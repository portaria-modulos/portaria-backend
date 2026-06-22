package com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos.BlocoEntity;

public record RequestBlocoDTO(Long id,String nome){
    public RequestBlocoDTO(BlocoEntity e) {
        this(e.getId(),e.getNome());
    }
}
