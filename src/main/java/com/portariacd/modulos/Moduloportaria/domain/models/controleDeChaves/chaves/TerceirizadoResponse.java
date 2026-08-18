package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves;

import jakarta.validation.constraints.NotBlank;

public class TerceirizadoResponse extends FactoryResponseChaves{
    @NotBlank
    String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
