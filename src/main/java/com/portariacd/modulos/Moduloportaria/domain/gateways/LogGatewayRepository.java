package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.LogAcaoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogGatewayRepository {
    public void registrarLog(UsuarioRequestDTO usuario, String acao, String descricao);

    Page<LogAcaoDTO> listaAcoes(Pageable page, String busca);
}
