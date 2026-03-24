package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.LogGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.LogAcaoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.adapters.buscaLog.BuscaLogsSpec;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.LogRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.log.LogAcaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class LogAdapter implements LogGatewayRepository {
    private final LogRepository repository;
    public LogAdapter(LogRepository repository){
        this.repository = repository;
    }
    @Override
    public void registrarLog(UsuarioRequestDTO usuario, String acao, String descricao) {
        LogAcaoEntity log = new LogAcaoEntity(usuario, acao, descricao,usuario.filial());
        repository.save(log);
    };
    @Override
    public Page<LogAcaoDTO> listaAcoes(Pageable page, String busca) {
        var spec = Specification.allOf(
              BuscaLogsSpec.busca(busca)
        );
        return (Page<LogAcaoDTO>)
                repository.findAll(spec,page)
                .stream().sorted(Comparator.comparing(LogAcaoEntity::getId).reversed())
                .map(LogAcaoDTO::new);
    }
};
