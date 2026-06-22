package com.portariacd.modulos.Moduloportaria.infrastructure.validation;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.RegistroVisitantePortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import org.springframework.stereotype.Component;

@Component
public class ValidaStatus  implements  ValidaStatusPortaria{


    @Override
    public void validEntrada(RegistroVisitantePortaria registroVisitantePortaria) {
        if(registroVisitantePortaria.getStatus().equals(StatusPortaria.SAIDA_LIBERADA)){
            throw new RuntimeException("Erro eo permitir entrada de Motorista");
        } else if (registroVisitantePortaria.getStatus().equals(StatusPortaria.RECUSADO)) {
            throw new RuntimeException("Erro eo permitir entrada de Motorista: "+StatusPortaria.RECUSADO);
        } else if (registroVisitantePortaria.getStatus().equals(StatusPortaria.AGUARDANDO_SAIDA)) {
            throw new RuntimeException("Erro eo permitir entrada de Motorista: "+StatusPortaria.AGUARDANDO_SAIDA);

        }
    }
    @Override
    public void validSaida(RegistroVisitantePortaria registroVisitantePortaria) {
        if(registroVisitantePortaria.getStatus().equals(StatusPortaria.SAIDA_LIBERADA)){
            throw new RuntimeException("Erro eo permitir entrada de Motorista");
        } else if (registroVisitantePortaria.getStatus().equals(StatusPortaria.RECUSADO)) {
            throw new RuntimeException("Erro eo permitir entrada de Motorista: "+StatusPortaria.RECUSADO);
        }
        else if (registroVisitantePortaria.getStatus().equals(StatusPortaria.AGUARDANDO_ENTRADA)) {
            throw new RuntimeException("Erro ao liberar Motorista atualize o status para: "+StatusPortaria.AGUARDANDO_SAIDA);

        }
    }
}
