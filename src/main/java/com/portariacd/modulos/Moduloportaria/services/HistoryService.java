package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.HistoryGatewayReposity;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.historyDTO.HistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryGatewayReposity reposity;
     public HistoryService(HistoryGatewayReposity reposity){
         this.reposity = reposity;
     }

    public Page<HistoryDTO> historico(Pageable page,String busca){
       return  reposity.historico(page,busca);
    }
    public void delete(Long id, Long usuarioId){
         reposity.deleteHistory(id,usuarioId);
    }

}
