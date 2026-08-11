package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioConsumerRequestDTO(Long id, @NotBlank String matricula, @NotBlank String GmcoreId, @NotBlank String nome, @NotBlank String setor, @NotNull Integer filial,@NotNull Long usuarioInsert,@NotBlank String imagemFacial
){
    public UsuarioConsumerRequestDTO(UsuarioConsumerChaves e) {
       this(e.getId(),e.getMatricula(),e.getGmcoreId(),e.getNome(),e.getSetor(),e.getFilial(),e.getUsuarioInsert(),null);
    }
}
