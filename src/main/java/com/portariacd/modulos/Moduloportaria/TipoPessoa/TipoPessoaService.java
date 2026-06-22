package com.portariacd.modulos.Moduloportaria.TipoPessoa;

import com.portariacd.modulos.Moduloportaria.domain.gateways.TipoPessoaGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaReuestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TipoPessoaService{
    private final TipoPessoaGatewayRepository repository;
    public TipoPessoaService(TipoPessoaGatewayRepository repository){
        this.repository = repository;
    }

    public void registro(TipoPessoaDTO response) {
        repository.registro(response);
    }

    public void atualiza(long idTipoPessoa, TipoPessoaDTO response) {
       repository.atualiza(idTipoPessoa,response);
    }

    public void delete(long id) {
      repository.delete(id);
    }

    public Map<String, List<TipoPessoaReuestDTO>> lista() {
        return repository.lista();
    }
}
