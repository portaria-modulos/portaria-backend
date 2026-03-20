package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.RegistroVisitantePortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.VisitanteDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;

import java.time.LocalDateTime;

public record RequestPortariaDTO(
        long id,
        String protocolo,
        String nomeCompleto,
        String placaVeiculo,
        String tipPessoa,
        Integer filialSocitado,
        String bloco,
        String status,
        String ocupacaoLiberada,
        String Obs,
        Boolean ativo,
        LocalDateTime dataCriacao,
        EntradaVisitanteDTO entrada,
        SaidaVisitanteDTO saida,
        VisitanteDTO visitante,
        UsuarioAutorizadoDTO autorizador,
         Boolean prioridadeAtrasoAtivo,
         String prioridadeAviso,
        String prioridadeAtraso) {
    public RequestPortariaDTO(RegistroVisitantePortaria e) {
        this(
                e.getId(),
                e.getProtocolo(),
                e.getNomeCompleto(),
                e.getPlacaVeiculo(),
                e.getVisitante().getTipoPessoa(),
                e.getFilial(),
                e.getBloco(),
                e.getStatus().name(),
                e.getOcupacaoLiberad(),
                e.getDescricao(),
                e.getAtivo(),
                e.getDataCriacao(),
                e.getEntradaVisitante()!=null ? new EntradaVisitanteDTO(e.getEntradaVisitante()):null,
                e.getSaidaVisitante()!=null ? new SaidaVisitanteDTO(e.getSaidaVisitante()):null,
                e.getVisitante()!=null ? new VisitanteDTO(e.getVisitante()):null,
                e.getCriadorId()!=null ? new UsuarioAutorizadoDTO(e.getCriadorId()):null,
                e.getPrioridadeAtrasoAtivo(),
                e.getPrioridadeAviso(),
                e.getPrioridadeAtraso()

        );
    }

    public RequestPortariaDTO(RegistroVisitantePortariaEntity e) {
        this(
                e.getId(),
                e.getProtocolo(),
                e.getNomeCompleto(),
                e.getPlacaVeiculo(),
                e.getVisitante().getTipoPessoa(),
                e.getFilialSolicitado(),
                e.getBloco(),
                e.getStatus().name(),
                e.getOcupacaoLiberada(),
                e.getDescricao(),
                e.getAtivo(),
                e.getDataCriacao(),
                e.getEntradaVisitante()!=null ? new EntradaVisitanteDTO(e.getEntradaVisitante()):null,
                e.getSaidaVisitante()!=null ? new SaidaVisitanteDTO(e.getSaidaVisitante()):null,
                e.getVisitante()!=null ? new VisitanteDTO(e.getVisitante()):null,
                e.getCriador()!=null ? new UsuarioAutorizadoDTO(e.getCriador()):null,
                e.getPrioridadeAtrasoAtivo(),
                e.getPrioridadeAviso(),
                e.getPrioridadeAtraso()


        );
    }

}
