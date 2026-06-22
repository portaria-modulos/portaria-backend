package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.services.LogAcaoService;
import com.portariacd.modulos.Moduloportaria.domain.gateways.HistoryGatewayReposity;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.historyDTO.HistoryDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.HistoryRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class HistoryAdapter implements HistoryGatewayReposity {
    private final HistoryRepository repository;
    private UsuarioRepository usuarioRepository;
    private final LogAcaoService service;
    public HistoryAdapter(HistoryRepository repository,LogAcaoService service,UsuarioRepository usuarioRepository){
        this.repository = repository;
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    @Transactional
    public Page<HistoryDTO> historico(Pageable page, String busca) {
        Page<HistoryDTO>  pageLista;
        if(busca!=null && !busca.isEmpty()){
            pageLista =  repository.findbyBusca(page,busca).map(HistoryDTO::new);
        }else {
            pageLista = repository.findAll(page).map(HistoryDTO::new);
        }
        return pageLista;
    }

    @Override
    public void deleteHistory(Long id,Long usuarioId) {
        var usuario =  usuarioRepository.findById(usuarioId);
        if(usuario.isPresent()){
            var usuarioCadastrado = new UsuarioRequestDTO(usuario.get());

          var historico =  repository.findById(id).orElseThrow(
                ()-> new RuntimeException("Id não encontrado")
            );

            if(historico.getStatus()!=null
                    && historico.getStatus().equals(StatusPortaria.SAIDA_LIBERADA)) {
                repository.delete(historico);
                service.registrarLog(
                        usuarioCadastrado,
                        "DELETE_HISTORY",
                        String.format(
                                "Usuário %s deletou histórico ID %d com status %s",
                                usuarioCadastrado.nome(),
                                historico.getId(),
                                historico.getStatus()
                        )
                );
            }
            else {
                throw new RuntimeException("Erro ao deletar histórico, histórico incompleto");
            }        }
    }
}
