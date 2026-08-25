package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import java.util.List;

public record ImportacaoUsuarioResponseDTO(
        int total,
        int cadastrados,
        int erros,
        List<ResultadoLinhaImportacaoDTO> detalhes) {
}
