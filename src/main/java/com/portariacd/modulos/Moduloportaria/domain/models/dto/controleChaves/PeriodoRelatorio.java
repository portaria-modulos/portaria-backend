package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves;

public enum PeriodoRelatorio {
    DIA,
    MES,
    ANO;

    public static PeriodoRelatorio from(String valor) {
        if (valor == null || valor.isBlank()) {
            return MES;
        }
        return valueOf(valor.trim().toUpperCase());
    }
}
