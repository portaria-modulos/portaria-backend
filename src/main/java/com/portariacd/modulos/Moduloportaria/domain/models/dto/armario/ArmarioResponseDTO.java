package com.portariacd.modulos.Moduloportaria.domain.models.dto.armario;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChaveHistory;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesResponseDTO;

import java.util.List;

public record ArmarioResponseDTO(Long id, Long filial, String tipo, List<BlocoChavesResponseDTO> armariosUnidade) {

    public ArmarioResponseDTO(Armario e) {
        this(e.getId(),e.getFilial(),e.getTipo().name(),e.getBlocoChaves().stream().map(BlocoChavesResponseDTO::new).toList());
    }


}
