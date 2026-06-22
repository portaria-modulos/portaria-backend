package com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;

public record RequestRecorrenciaDTO(Long id, String nome){
    public RequestRecorrenciaDTO(Recorrencia e) {
        this(e.getId(),e.getNome());
    }
}
