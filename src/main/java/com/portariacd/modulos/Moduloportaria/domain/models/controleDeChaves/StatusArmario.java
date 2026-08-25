package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import java.text.Normalizer;

public enum StatusArmario {
    OCUPADO("acupado"),
    LIVRE("livre"),
    EM_MANUTENCAO("manutencao"),
    BLOQUEADO("bloqueado"),
    INATIVO("inativo"),
    SEM_ACESSO("sem_acesso"),
    SEM_CHAVE("sem_chave");
    private final String tipo;

    StatusArmario(String a) {
        this.tipo = a;
    }

    public static StatusArmario converteTipoArmario(String resposta) {
        if (resposta == null || resposta.isBlank()) {
            throw new RuntimeException("Tipo de status não informado");
        }
        String normalizado = Normalizer.normalize(resposta.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace(' ', '_')
                .replace('-', '_')
                .toUpperCase();
        if ("MANUTENCAO".equals(normalizado)) {
            return EM_MANUTENCAO;
        }
        try {
            return valueOf(normalizado);
        } catch (IllegalArgumentException ignored) {
            // Mantém compatibilidade com os valores antigos gravados no banco.
        }
        for (StatusArmario t : StatusArmario.values()) {
            if (t.tipo.equalsIgnoreCase(resposta.trim())) {
                return t;
            }
        }
        throw new RuntimeException("Tipo informado e invalido");
    }
}
