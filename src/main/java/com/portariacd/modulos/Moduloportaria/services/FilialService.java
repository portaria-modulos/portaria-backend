package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.FilialInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RegistroFilialDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RequestFilialDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilialService {
    private final FilialInterfaceGateway FilialInterfaceGateway;
    public FilialService(FilialInterfaceGateway FilialInterfaceGateway){
        this.FilialInterfaceGateway = FilialInterfaceGateway;
    }
    public Map<String, List<RequestFilialDTO>> lista(){
        Map<String, List<RequestFilialDTO>> listMap = new HashMap<>();
        var lista = FilialInterfaceGateway.lista();
        listMap.put("filial",lista);
        return listMap;
    }
    public void registro(RegistroFilialDTO registroFilialDTO){
        FilialInterfaceGateway.registroFilial(registroFilialDTO);
    }
}
