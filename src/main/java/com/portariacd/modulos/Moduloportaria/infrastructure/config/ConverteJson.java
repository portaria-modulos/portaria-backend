package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.FactoryResponseChavesDeserializer;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.FactoryResponseChaves;
import org.springframework.stereotype.Component;

@Component
public class ConverteJson {

    public <T> T conversor(String data,Class<T> className){
        Gson gson = new Gson();
        return gson.fromJson(data,className);
    }
    public <T> T conversorChaves(String data, Class<T> className) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FactoryResponseChaves.class, new FactoryResponseChavesDeserializer())
                .create();

        return gson.fromJson(data, className);
    }
}
