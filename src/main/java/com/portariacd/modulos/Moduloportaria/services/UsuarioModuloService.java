package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.ModuloGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioModuloService {
    private ModuloGatewayRepository repository;
    public UsuarioModuloService(ModuloGatewayRepository repository){
        this.repository = repository;
    }
    public void addPermission(@Valid List<ModuleDTO> pemission, FilialDTOMod mod, Long usuarioId){
        repository.addPermission(pemission,mod,usuarioId);
    }
    public List<SistemaAcessoUsuarioDTO> lista(){
       return repository.listaPermission();
    }
}
