package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves;

import jakarta.validation.constraints.NotBlank;

public class UsuarioConsumerRequestDTO extends FactoryResponseChaves {
    @NotBlank
    String matricula;
    @NotBlank
    String gmId;
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getGmId() {
        return gmId;
    }
    public void setGmId(String gmId) {
        this.gmId = gmId;
    }
}
