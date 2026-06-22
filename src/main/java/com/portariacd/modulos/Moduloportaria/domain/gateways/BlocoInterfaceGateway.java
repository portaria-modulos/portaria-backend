package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RegistroBlocoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RequestBlocoDTO;

import java.util.List;

public interface BlocoInterfaceGateway {
     void registroBloco(RegistroBlocoDTO registro);
    public List<RequestBlocoDTO> lista();

    }
