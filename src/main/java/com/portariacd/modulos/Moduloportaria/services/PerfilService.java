package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.PerfilGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class PerfilService {
    private final PerfilGatewayRepository repository;
    public  PerfilService(PerfilGatewayRepository repository){
        this.repository = repository;
    }
    public Map<String, List<PerfilResponseDTO>> listaPerfils() {
        return repository.listaPerfils();
    }
}
