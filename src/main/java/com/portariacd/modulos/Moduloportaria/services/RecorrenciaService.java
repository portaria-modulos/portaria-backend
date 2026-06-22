package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.RecorrenciaInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecorrenciaService {
    private final RecorrenciaInterfaceGateway RecorrenciaInterfaceGateway;
    public RecorrenciaService(RecorrenciaInterfaceGateway RecorrenciaInterfaceGateway){
        this.RecorrenciaInterfaceGateway = RecorrenciaInterfaceGateway;
    }
    public Map<String, List<RequestRecorrenciaDTO>> lista(Long usuarioID){
        Map<String, List<RequestRecorrenciaDTO>> listMap = new HashMap<>();
        var lista = RecorrenciaInterfaceGateway.lista(usuarioID);
        listMap.put("recorrencia",lista);
        return listMap;
    }
}
