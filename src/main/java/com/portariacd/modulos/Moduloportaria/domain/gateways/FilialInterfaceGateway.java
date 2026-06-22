package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RegistroFilialDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RequestFilialDTO;

import java.util.List;

public interface FilialInterfaceGateway {
     void registroFilial(RegistroFilialDTO registro);
    public List<RequestFilialDTO> lista();

    }
