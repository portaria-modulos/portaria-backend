package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizaRegistro(
        @NotNull
        long id,
        @NotBlank
        String nomeCompleto,
        @NotBlank
        String numeroTelefone,
        @NotBlank
        String placaVeiculo,
        @NotBlank
        String bloco,
        String  tipoPessoa,
        @NotBlank
        String tipoDeAcesso,
        String dataAcesso,
        String ocupacaoLiberada
) {
}

