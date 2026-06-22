package com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante;


import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.log.LogAcaoEntity;

import java.time.LocalDateTime;
public record LogAcaoDTO(
        Long id,
        Long usuarioId,
        String usuario,
        String acao,
        String descricao,
        LocalDateTime dataHora,
        Integer filial
) {


    public LogAcaoDTO(LogAcaoEntity e) {
        this(e.getId(),e.getUsuarioId(),e.getUsuario(),e.getAcao(),e.getDescricao(),e.getDataHora(),e.getFilial());
    }
}
