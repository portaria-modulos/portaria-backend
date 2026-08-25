package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves;

import java.util.List;

public record RelatorioEntregaChaveDTO(
        List<ResumoEntregaChaveDTO> resumo,
        List<EntregaChaveRelatorioDTO> entregas
) {
}
