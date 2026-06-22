package com.portariacd.modulos.Moduloportaria.infrastructure.validation;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.RegistroVisitantePortaria;

public interface ValidaStatusPortaria {
    public void validEntrada(RegistroVisitantePortaria registroVisitantePortaria);
    public void validSaida(RegistroVisitantePortaria registroVisitantePortaria);

}
