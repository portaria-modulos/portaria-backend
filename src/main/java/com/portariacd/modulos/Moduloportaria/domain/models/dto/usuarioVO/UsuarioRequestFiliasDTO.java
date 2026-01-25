package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;

import java.util.List;

public record UsuarioRequestFiliasDTO(
        long id,
         Integer filial,
        List<SistemaAcessoFilialUsuarioDTO> acess
) {
    public UsuarioRequestFiliasDTO(UsuarioEntity usuario) {
        this(usuario.getId()
                ,usuario.getFilial(),
                usuario.getFiliais()!=null?usuario.getFiliais().stream().map(e->new SistemaAcessoFilialUsuarioDTO(e.getFilial())).toList():null
        );
    }

//    public UsuarioRequestFiliasDTO(Usuario usuario) {
//        this(
//                usuario.getId(),
//                usuario.getFilial(),
//        );
//
//    }


}
