package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.PerfilGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.PerfilRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PerfilAdapter implements PerfilGatewayRepository {
    private final PerfilRepository repository;
    public  PerfilAdapter(PerfilRepository perfilRepository){
        this.repository  = perfilRepository;

    }

    @Override
    public Map<String,List<PerfilResponseDTO>> listaPerfils() {
        Map<String,List<PerfilResponseDTO>> listMap = new HashMap<>();
        List<PerfilResponseDTO> lista =  repository.findAll().stream().map(PerfilResponseDTO::new).toList();
        listMap.put("perfis",lista);
        return listMap;
    }
}
