package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.UsuarioGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.auth.AlteraSenhaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.CadastroUsuarioDto;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UsuarioService{
    private final UsuarioGatewayRepository repository;
    public UsuarioService(UsuarioGatewayRepository repository){
        this.repository = repository;
    }

    public void registroUsuario(CadastroUsuarioDto dto) {
       repository.registroUsuario(dto);
    }

    public Page<UsuarioRequestDTO> listaUsuario(Pageable page,String busca) {
        return repository.listaUsuario(page,busca);
    }

    public TokenResponse authLogin(@Valid UsuarioLoginDTO dto) {
        return repository.authLogin(dto);
    }
    public Map<String, UsuarioRequestPerfilDTO> buscaUsuarioPerfil(String email) {
        return repository.buscaUsuarioPerfil(email);
    }


    public String adicionarPerfil(long usuarioId, Long perfilId,Boolean ativo) {
       return repository.adicionarPerfil(usuarioId,perfilId,ativo);
    }
    public String AlteraSenha(String email){
        return repository.AlteraSenha(email);
    }

    public UsuarioBuscaRequestDTO buscaUsuarioId(Long id) {
        return repository.buscarUsuarioId(id);
    }
    public UsuarioRequestDTO buscaUsuario(Long id)
    {
        return repository.buscarUsuario(id);
    }

    public void salvaImagem(Long usuarioId, MultipartFile file) {
         repository.salvaImagem(usuarioId,file);
    }
    public void alteraSenha(AlteraSenhaDTO dto){
        repository.alteraSenha(dto);
    }

    public UsuarioRequestFiliasDTO filtraFilial(Long id) {
        return repository.filtraFiliais(id);
    }
}

