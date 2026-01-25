package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.FiliaisModulosGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.gateways.ModuloGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.FiliaisModulesDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioModuloFilialService {
    private FiliaisModulosGatewayRepository repository;
    public UsuarioModuloFilialService(FiliaisModulosGatewayRepository repository){
        this.repository = repository;
    }
    public void addPermission(@Valid List<FiliaisModulesDTO> pemission, Long usuarioId){
        repository.addPermission(pemission,usuarioId);
    }
    public List<SistemaAcessoFilialUsuarioDTO> lista(){
       return repository.listaPermission();
    }
}
