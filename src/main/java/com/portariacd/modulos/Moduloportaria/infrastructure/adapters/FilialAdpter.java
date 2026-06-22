package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.FilialInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RegistroFilialDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RequestFilialDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.FilialRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.Filial;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilialAdpter implements FilialInterfaceGateway {
    private FilialRepository repository;
    public FilialAdpter(FilialRepository repository
                             ){
        this.repository = repository;
    }
    @Override
    public void registroFilial(RegistroFilialDTO registro){
            if (repository.findByNumeroFilial(registro.numeroFilial(),registro.nome()).isPresent()) {
                throw new RuntimeException("Numero ou Nome '" + registro.numeroFilial() + "' '" + registro.nome() + "' já cadastrado");
            }
        Filial filial = new Filial(registro.nome(),registro.numeroFilial());
        repository.save(filial);
    }

    @Override
    public List<RequestFilialDTO> lista(){
       return repository.findAll().stream()
               .map(RequestFilialDTO::new)
               .collect(Collectors.toList());
    }
}
