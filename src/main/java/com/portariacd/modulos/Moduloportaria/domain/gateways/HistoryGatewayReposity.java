package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.historyDTO.HistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryGatewayReposity {
    Page<HistoryDTO> historico(Pageable page,String busca);
    void deleteHistory(Long id,Long usuarioId);
}
