package com.portariacd.modulos.Moduloportaria.infrastructure.adapters.filiasModulos;

import com.portariacd.modulos.Moduloportaria.domain.gateways.FiliaisModulosGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.FilialRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.FiliaisModulesDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.UsuarioFiliaisRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.UsuarioFilialEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioFiliasAdapter implements FiliaisModulosGatewayRepository {
  private UsuarioFiliaisRepository repository;
  private FilialRepository respositoryAcesso;
  private UsuarioRepository usuarioRepository;
  public UsuarioFiliasAdapter(UsuarioFiliaisRepository repository,
                              FilialRepository respositoryAcesso
                               , UsuarioRepository usuarioRepository
  ){
      this.repository = repository;
      this.respositoryAcesso = respositoryAcesso;
      this.usuarioRepository = usuarioRepository;
  }

  public void addPermission(List<FiliaisModulesDTO> pemission, Long usuarioId){
      UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow(
              ()-> new RuntimeException("Erro ao buscar Usuario")
      );
      for (FiliaisModulesDTO modulo:pemission){
       var acesso = respositoryAcesso.findById(modulo.permissionId());
          Optional<UsuarioFilialEntity> existente =
                  repository.findByUsuarioIdAndFilialId(usuarioId, modulo.permissionId());
          UsuarioFilialEntity entity;
          if(existente.isPresent()) {
              entity =existente.get();
              entity.setAtivo(modulo.ativo());
              repository.save(entity);
       }else{
              entity = new UsuarioFilialEntity(usuario, acesso.get());
              entity.setAtivo(modulo.ativo());
              repository.save(entity);
          }
      }
  }
    @Override
    public List<SistemaAcessoFilialUsuarioDTO> listaPermission() {
       return   respositoryAcesso.findAll().stream().map(SistemaAcessoFilialUsuarioDTO::new).toList();
    }
}
