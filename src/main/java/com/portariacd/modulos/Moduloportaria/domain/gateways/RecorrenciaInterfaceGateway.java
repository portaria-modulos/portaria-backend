package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RegistroRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;

import java.util.List;

public interface RecorrenciaInterfaceGateway {
     void registroRecorrencia(RegistroRecorrenciaDTO registro);
    public List<RequestRecorrenciaDTO> lista(Long usuarioId);

    }
