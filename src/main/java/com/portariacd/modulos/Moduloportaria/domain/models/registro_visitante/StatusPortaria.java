package com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante;

public enum StatusPortaria {
    AGUARDANDO_ENTRADA("aguardando entrada"),
    ENTRADA_LIBERADA("entrada liberada"),
    AGUARDANDO_SAIDA("aguardando saida"),
    SAIDA_LIBERADA("liberado"),
    RECUSADO("recusado"),
    FECHADO_AUTOMATICO("fechado");
    private String nome;
    StatusPortaria(String nome){
        this.nome = nome;
    }

    public static StatusPortaria buscaStatus(String status){
        for(StatusPortaria tipo: values()){
            if(tipo.nome.equals(status)){
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de acesso inválido: " + status);

    }

}
