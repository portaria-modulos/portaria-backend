package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante;

public enum StatusTipoDeAcesso {
    RECORRENTE("recorrente"),
    UNICO("unico"),
    RECORRENCIA_TEMPORARIA("recorrencia temporaria");
    private final String nome;

    StatusTipoDeAcesso(String nome) {
        this.nome = nome;
    }
    public static StatusTipoDeAcesso StatusAdd(String nome){
      for (StatusTipoDeAcesso tipo :values()){
          if(tipo.nome.equals(nome.toLowerCase())){
              return tipo;
          }
      }
        throw new IllegalArgumentException("Tipo de acesso inválido: " + nome);

    }
}
