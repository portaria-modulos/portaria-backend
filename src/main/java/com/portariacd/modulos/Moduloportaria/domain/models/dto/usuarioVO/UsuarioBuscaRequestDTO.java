package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;

public record UsuarioBuscaRequestDTO(
        long id,
         String nome,
         String email,
         String ocupacaoOperacional,
         Integer filial,
        String avatar
) {
    public UsuarioBuscaRequestDTO(UsuarioEntity usuario) {
        this(usuario.getId(),usuario.getNome()
                ,usuario.getEmail()
                ,usuario.getOcupacaoOperacional()
                ,usuario.getFilial(),
                usuario.getAvatar()
        );
    }


}
