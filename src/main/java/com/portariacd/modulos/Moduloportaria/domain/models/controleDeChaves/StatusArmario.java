package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

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
        for (StatusArmario t : StatusArmario.values()) {
            if (t.tipo.equalsIgnoreCase(resposta)) {
                return t;
            }
        }
        throw new RuntimeException("Tipo informado e invalido");
    }
}
