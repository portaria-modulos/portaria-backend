package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;

import java.util.List;

public record UsuarioRequestPerfilDTO(
        long id,
         String nome,
         String email,
         boolean ativo,
         Integer filial,
         PerfilResponseDTO perfil,
        List<SistemaAcessoUsuarioDTO> acess
) {
    public UsuarioRequestPerfilDTO(UsuarioEntity usuario) {
        this(usuario.getId(),usuario.getNome()
                ,usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getFilial()
                ,usuario.getPerfil()!=null?new PerfilResponseDTO(usuario.getPerfil()):null,
                usuario.getModulos()!=null?usuario.getModulos().stream().map(e->new SistemaAcessoUsuarioDTO(e.getModulo())).toList():null

        );
    }



}
