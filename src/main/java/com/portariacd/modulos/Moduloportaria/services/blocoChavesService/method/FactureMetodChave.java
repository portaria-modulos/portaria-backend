package com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.DesvolucaoChaveDto;
import jakarta.validation.constraints.NotBlank;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "tipo", // será "PF" ou "PJ"
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DesvolucaoChaveDto.class, name = "not_consumer"),
        @JsonSubTypes.Type(value = DesvolucaoChaveUsuarioDto.class, name = "consumer")
})
public interface FactureMetodChave {
    @NotBlank(message = "Campo obrigatorio")
     String tipo();}