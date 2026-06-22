package com.portariacd.modulos.Moduloportaria.infrastructure.facture;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.AtualizaRegistroPortariaDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "typeMethod",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AtualizaRegistroPortariaDTO.class, name = "VISITANTE"),
        @JsonSubTypes.Type(value = RegistroPortariaRequestDTO.class, name = "NAO_VISITANTE")

})
public abstract class CadastroTypeFacture {
    @NotBlank
    @Valid
    private String typeMethod;
    public String getTypeMethod(){
        return  typeMethod;
    }
    public void setTypeMethod(String type){
        typeMethod = type;
    }
}
