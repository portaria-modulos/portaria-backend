package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;
public record UsuarioConsumerResponseDTO(Long id, String matricula, String GmcoreId, String nome, String setor, Integer filial, Long usuarioInsert, String imagemFacial){
    public UsuarioConsumerResponseDTO(UsuarioConsumerChaves e) {
        this(e.getId(),e.getMatricula(),e.getGmcoreId(),e.getNome(),e.getSetor(),e.getFilial(),e.getUsuarioInsert(),null);
    }
}
