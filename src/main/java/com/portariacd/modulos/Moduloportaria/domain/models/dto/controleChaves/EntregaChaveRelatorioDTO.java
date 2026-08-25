package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves;

import java.time.OffsetDateTime;

public record EntregaChaveRelatorioDTO(
        Long id,
        Long filial,
        Long armarioId,
        String modeloArmario,
        Integer numeroChave,
        String nomeColaborador,
        String matriculaColaborador,
        String empresaColaborador,
        OffsetDateTime dataHoraRetirada,
        java.time.LocalDateTime dataHoraDevolucao,
        String usuarioRetirada,
        String usuarioDevolucao,
        String status,
        Long slaMinutos
) {
}
