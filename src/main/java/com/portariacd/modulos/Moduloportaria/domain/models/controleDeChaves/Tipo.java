package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Tipo {
    VESTIARIO_FEMININO("feminino"),
    VESTIARIO_MASCULINO("masculino"),
    PORTARIA_SECOS("secos"),
    PORTARIA_FRIOS("frios"),
    PORTARIA_HORT("hort");

    private final String tipo;


     Tipo(String s){
         this.tipo = s;
     }

     public static Tipo convertTipo(String s){
         for (Tipo v : Tipo.values()){
             if(v.tipo.equalsIgnoreCase(s)){
                return v;
             }
         }
         String tiposValidos = Arrays.stream(Tipo.values())
                 .map(tipo -> tipo.tipo)
                 .collect(Collectors.joining(", "));
         throw new RuntimeException("Tipo de Armario invalido, Informa um dois tipos: "+ tiposValidos);
     }


}
