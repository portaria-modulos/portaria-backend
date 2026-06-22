package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;

import java.util.List;
import java.util.Map;

public interface PerfilGatewayRepository {
    public Map<String,List<PerfilResponseDTO>> listaPerfils();
}
