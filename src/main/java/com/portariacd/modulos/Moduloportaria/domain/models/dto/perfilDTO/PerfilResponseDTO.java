package com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.Perfil;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.PerfilEntity;

public record PerfilResponseDTO(Long id, String nome) {
    public PerfilResponseDTO(PerfilEntity e) {
        this(e.getId(),e.getNome());
    }

    public PerfilResponseDTO(Perfil perfil) {
        this(perfil.getId(),perfil.getNome());
    }
}
