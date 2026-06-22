package com.portariacd.modulos.Moduloportaria.domain.models.visitante;

public enum StatusTypeDeleteVisitante {
    DELETE("delete"),
    BLOQUEIO("bloqueio"),
    DESBLOQUEIO("desbloqueio");
    private final String nome;
    StatusTypeDeleteVisitante(String nome) {
        this.nome = nome;
    }
    public static StatusTypeDeleteVisitante StatusAdd(String nome){
        for (StatusTypeDeleteVisitante tipo :values()){
            if(tipo.nome.equals(nome.toLowerCase())){
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de acesso inválido: " + nome);

    }
}
