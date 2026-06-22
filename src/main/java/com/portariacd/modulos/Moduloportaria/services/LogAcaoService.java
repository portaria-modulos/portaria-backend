package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.LogGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.LogAcaoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LogAcaoService {
    private final LogGatewayRepository repository;
    public LogAcaoService(LogGatewayRepository repository){
        this.repository = repository;
    }
    public void registrarLog(UsuarioRequestDTO usuario, String acao, String descricao){
       repository.registrarLog(usuario,acao,descricao);

    }

    public Page<LogAcaoDTO> listaAcoes(Pageable page, String busca) {
      return   repository.listaAcoes(page,busca);
    }
}
