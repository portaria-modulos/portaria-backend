package com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.Usuario;

public record ResponseUsuarioDTO(
         String nome,
         String email,
         String ocupacaoOperacional,
         Integer filial
) {
    public ResponseUsuarioDTO(Usuario e) {
        this(e.getNome(),e.getEmail(),e.getOcupacaoOperacional(), e.getFilial());
    }


}
