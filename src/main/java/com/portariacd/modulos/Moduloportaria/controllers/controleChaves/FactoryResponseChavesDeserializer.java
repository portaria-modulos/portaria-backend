package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.google.gson.*;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.FactoryResponseChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.TerceirizadoResponse;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerRequestDTO;

import java.lang.reflect.Type;

public class FactoryResponseChavesDeserializer implements JsonDeserializer<FactoryResponseChaves> {
    @Override
    public FactoryResponseChaves deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement tipoElement = jsonObject.get("tipo");

        if (tipoElement == null || tipoElement.isJsonNull()) {
            throw new JsonParseException("O campo 'tipo' é obrigatório para deserialização.");
        }

        String tipo = tipoElement.getAsString();

        if ("funcionario".equalsIgnoreCase(tipo)) {
            return context.deserialize(jsonObject, UsuarioConsumerRequestDTO.class);
        } else if ("terceirizado".equalsIgnoreCase(tipo)) {
            return context.deserialize(jsonObject, TerceirizadoResponse.class);
        } else {
            throw new JsonParseException("Tipo de colaborador desconhecido: " + tipo);
        }
    }
}