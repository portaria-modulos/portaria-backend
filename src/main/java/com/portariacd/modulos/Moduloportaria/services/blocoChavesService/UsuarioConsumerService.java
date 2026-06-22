package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioConsumerService {
    @Autowired
    private UsuarioConsumerRepository repository;
    public Map<String, String> cadastroDeUsuario(UsuarioConsumerRequestDTO usm){
        var usuario = repository.findByUsuario(usm.GmcoreId(),usm.matricula());
        if(usuario.isPresent()){

            if(!usuario.get().getAtivo()){
                throw new RuntimeException("Usuario bloqueado");
            }

            throw new RuntimeException("Colaborador ja cadastrado");
        }

        repository.save(new UsuarioConsumerChaves(usm));

     return Map.of("msg","Usuario criado com sucesso");
    }

    public List<UsuarioConsumerRequestDTO> lista() {
       return repository.findAll().stream().map(UsuarioConsumerRequestDTO::new).toList();
    }
}
