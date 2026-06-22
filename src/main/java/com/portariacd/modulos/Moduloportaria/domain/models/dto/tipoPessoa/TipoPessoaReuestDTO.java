package com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.tipoPessoa.TipoPessoaEntity;

public record TipoPessoaReuestDTO(Long id, String nome) {
    public TipoPessoaReuestDTO(TipoPessoaEntity e) {
        this(e.getId(),e.getNome());
    }
}
