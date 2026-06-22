package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.TipoPessoaGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaReuestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.TipoPessoaRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.tipoPessoa.TipoPessoaEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TipoPessoaAdpter implements TipoPessoaGatewayRepository {
    private final TipoPessoaRepository repository;
    public TipoPessoaAdpter(TipoPessoaRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public void registro(TipoPessoaDTO response) {
       var responseExiste = repository.findByNome(response.nome());
       if(responseExiste.isPresent()){
           throw new RuntimeException("Não foi possivel salvar tipo Pessoa: já existe");
       }
       repository.save(new TipoPessoaEntity(response.nome()));

    }

    @Override
    @Transactional
    public void atualiza(long idTipoPessoa, TipoPessoaDTO response) {
        var responseExiste = repository.findByNome(response.nome());
        if(responseExiste.isPresent()){
            throw new RuntimeException("Não foi possivel salvar tipo Pessoa: já existe");
        }
       var encontrado = repository.findById(idTipoPessoa).orElseThrow(
               ()->new RuntimeException("Nao foi possivel encontrar tipo Pessoa")
       );
        encontrado.atualiza(response.nome());
        repository.save(encontrado);
    }

    @Override
    @Transactional
    public void delete(long id) {
        var encontrado = repository.findById(id).orElseThrow(
                ()-> new RuntimeException("Id não encontrado")
        );

         repository.delete(encontrado);
    }
    @Override
    public Map<String, List<TipoPessoaReuestDTO>> lista() {
        Map<String, List<TipoPessoaReuestDTO>> listMap = new HashMap<>();
       List<TipoPessoaReuestDTO> lista = repository.findAllByOrderByIdAsc().stream().map(TipoPessoaReuestDTO::new).toList();
       listMap.put("tipoPessoa",lista);
       return listMap;
    }
}
