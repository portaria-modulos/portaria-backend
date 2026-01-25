package com.portariacd.modulos.Moduloportaria.infrastructure.adapters.Modulos;

import com.portariacd.modulos.Moduloportaria.domain.gateways.ModuloGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.FilialDTOMod;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.RespositorySistemaAcesso;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.ModuleDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.UsuarioModuloEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.UsuarioModuloRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioModuloAdapter implements ModuloGatewayRepository {
  private UsuarioModuloRepository repository;
  private RespositorySistemaAcesso respositoryAcesso;
  private UsuarioRepository usuarioRepository;
  public  UsuarioModuloAdapter(UsuarioModuloRepository repository,
                               RespositorySistemaAcesso respositoryAcesso
                               ,UsuarioRepository usuarioRepository
  ){
      this.repository = repository;
      this.respositoryAcesso = respositoryAcesso;
      this.usuarioRepository = usuarioRepository;
  }
 @Transactional
  public void addPermission(List<ModuleDTO> pemission, FilialDTOMod modFilial, Long usuarioId){
      UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow(
              ()-> new RuntimeException("Erro ao buscar Usuario")
      );
      if(modFilial.filial()!=null){
          usuario.setFilial(modFilial.filial());
          usuarioRepository.save(usuario);
      }
      for (ModuleDTO modulo:pemission){

       var acesso = respositoryAcesso.findById(modulo.permissionId());
          Optional<UsuarioModuloEntity> existente =
                  repository.findByUsuarioIdAndModuloId(usuarioId, modulo.permissionId());
          UsuarioModuloEntity entity;
          if(existente.isPresent()) {
              entity =existente.get();
              entity.setAtivo(modulo.ativo());
              repository.save(entity);
       }else{
              entity = new UsuarioModuloEntity(usuario, acesso.get());
              entity.setAtivo(modulo.ativo());
              repository.save(entity);

          }

      }
  }

    @Override
    public List<SistemaAcessoUsuarioDTO> listaPermission() {
       return   respositoryAcesso.findAll().stream().map(SistemaAcessoUsuarioDTO::new).toList();
    }

}
