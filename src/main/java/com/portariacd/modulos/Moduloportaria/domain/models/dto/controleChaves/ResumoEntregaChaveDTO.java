package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves;

public record ResumoEntregaChaveDTO(
        String periodo,
        long quantidadeEntregas,
        long quantidadeDevolvidas,
        long quantidadeEmAberto
) {
}
