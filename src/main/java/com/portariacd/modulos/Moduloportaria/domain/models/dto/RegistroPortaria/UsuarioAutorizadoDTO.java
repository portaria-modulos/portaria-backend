package com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.Usuario;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioAutorizadoDTO(
        long id,
         String nome,
         String avatar,
         String email,
         String ocupacaoOperacional,
         Integer filial) {
    public UsuarioAutorizadoDTO(UsuarioEntity usuario) {
        this(usuario.getId(),usuario.getNome(),
                usuario.getAvatar()!=null? usuario.getAvatar() : null
                ,usuario.getEmail()
                ,usuario.getOcupacaoOperacional()
                ,usuario.getFilial());
    }

    public UsuarioAutorizadoDTO(Usuario usuario) {
        this(usuario.getId(),usuario.getNome(),
                usuario.getAvatar()!=null? usuario.getAvatar() : null
                ,usuario.getEmail()
                ,usuario.getOcupacaoOperacional()
                ,usuario.getFilial()
        );

    }


}
