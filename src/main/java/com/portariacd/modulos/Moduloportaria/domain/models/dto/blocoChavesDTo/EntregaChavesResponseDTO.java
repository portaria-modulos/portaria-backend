package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record EntregaChavesResponseDTO(
 String nomeColaborador,
 String matriculaColaborador,
 OffsetDateTime dataHoraRetirada,
 LocalDateTime dataHoraDevolucao,
 String usuarioPortariaRetirada,
 Long usuarioIdRetirada,
 String usuarioPortariaDevolucao,
 Long usuarioIdDevolucao,
 Long filialId,
 Boolean entregue,
 BlocoChavesResponseDTO armario
) {
    public EntregaChavesResponseDTO(EntregaChave e) {
        this(e.getNomeColaborador(),
                e.getMatriculaColaborador()!=null?e.getMatriculaColaborador():null
                ,e.getDataHoraRetirada(),
                e.getDataHoraDevolucao(),
                e.getUsuarioPortariaRetirada(),
                e.getUsuarioIdRetirada(),
                e.getUsuarioPortariaDevolucao(),
                e.getUsuarioIdDevolucao(),
                e.getFilialId(),
                e.getEntregue(),
                new BlocoChavesResponseDTO(e.getBlocoChaves())
        );
    }
}
