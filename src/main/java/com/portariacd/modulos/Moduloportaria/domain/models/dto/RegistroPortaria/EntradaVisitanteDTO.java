package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.EntradaVisitante;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;

import java.time.LocalDateTime;

public record EntradaVisitanteDTO(
        LocalDateTime dataEntrada,String nomeFiscal,Long fiscalEntradaId,String imagem,Integer filial) {


    public EntradaVisitanteDTO(EntradaVisitante e) {
        this(e.getDataEntrada(),e.getNomeFiscal(),e.getFiscalEntradaId(),e.getImagem(),e.getFilial());
    }

    public EntradaVisitanteDTO(EntradaVisitanteEntity e) {
        this(e.getDataEntrada(),e.getNomeFiscal(),e.getFiscalEntradaId(),e.getImagem(),e.getFilial());
    }
}
