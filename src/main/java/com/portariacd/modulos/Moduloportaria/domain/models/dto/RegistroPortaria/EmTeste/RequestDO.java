package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste;

import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import jakarta.validation.Valid;

public class RequestDO {
    @Valid
   public CadastroTypeFacture type;

    public CadastroTypeFacture getType() {
        return type;
    }

    public void setType(CadastroTypeFacture type) {
        this.type = type;
    }
}
