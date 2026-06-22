package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.RecorrenciaInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RegistroRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.RecorrenciaRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.PermissionEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecorrenciaAdpter implements RecorrenciaInterfaceGateway {
    private RecorrenciaRepository repository;
    private UsuarioRepository repositoryUsuario;
    private static final Set<String> PERMISSOES_FULL_ACCESS = Set.of(
            "GERENCIAR_USUARIOS"
    );
    public RecorrenciaAdpter(RecorrenciaRepository repository,
                             UsuarioRepository repositoryUsuario
                             ){
        this.repository = repository;
        this.repositoryUsuario = repositoryUsuario;
    }
    @Override
    public void registroRecorrencia(RegistroRecorrenciaDTO registro){
            if (repository.findByNome(registro.nome()).isPresent()) {
                throw new RuntimeException("Nome '" + registro.nome() + "' já cadastrado");
            }
        Recorrencia bloco = new Recorrencia(registro.nome());
        repository.save(bloco);
    }


    @Override
    public List<RequestRecorrenciaDTO> lista(Long usuarioId){
        var usuario = repositoryUsuario.findById(usuarioId).orElseThrow(
                ()-> new RuntimeException("não foi possivel encontrar usuario")
        );
        var permission = usuario.getPerfil().getPermissoes();
       return repository.findAll().stream()
               .filter(rec->usuarioTemAcessoARecorrencia(rec,permission))
               .map(RequestRecorrenciaDTO::new)
               .collect(Collectors.toList());
    }

    private boolean usuarioTemAcessoARecorrencia(Recorrencia rec, Set<PermissionEntity> permissoes) {
     var permiss =   permissoes.stream()
             .map(p -> p.getName().toUpperCase())
             .anyMatch(PERMISSOES_FULL_ACCESS::contains);
        if(permiss){
         return true;
     }
     return rec.getNome() != null && rec.getNome().equalsIgnoreCase("unico");

    }
}
