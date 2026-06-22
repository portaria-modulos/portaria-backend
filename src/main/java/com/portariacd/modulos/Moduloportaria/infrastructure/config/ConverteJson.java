package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class ConverteJson {

    public <T> T conversor(String data,Class<T> className){
        Gson gson = new Gson();
        return gson.fromJson(data,className);
    }
}
