package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.FiliaisModulosGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.FiliaisModulesDTO;
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

    public List<SistemaAcessoFilialUsuarioDTO> listafiliaisUsuario(Long id) {
       return repository.listaFiliaisUsuario(id);
    }
}
