package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.BlocoInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RequestBlocoDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlocoService {
    private final BlocoInterfaceGateway blocoInterfaceGateway;
    public BlocoService(BlocoInterfaceGateway blocoInterfaceGateway){
        this.blocoInterfaceGateway = blocoInterfaceGateway;
    }
    public Map<String, List<RequestBlocoDTO>> lista(){
        Map<String, List<RequestBlocoDTO>> listMap = new HashMap<>();
        var lista = blocoInterfaceGateway.lista();
        listMap.put("blocos",lista);
        return listMap;
    }
}
