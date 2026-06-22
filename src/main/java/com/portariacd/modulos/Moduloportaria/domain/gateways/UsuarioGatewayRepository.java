package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.AlteraSenhaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.CadastroUsuarioDto;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UsuarioGatewayRepository {
     void registroUsuario(CadastroUsuarioDto dto);
     Page<UsuarioRequestDTO> listaUsuario(Pageable page, String busca);

     TokenResponse authLogin(@Valid UsuarioLoginDTO dto);

     Map<String,UsuarioRequestPerfilDTO> buscaUsuarioPerfil(String email);

     String adicionarPerfil(long usuarioId, Long perfilId,Boolean ativo);
     String AlteraSenha(String email);

     UsuarioBuscaRequestDTO buscarUsuarioId(Long id);
     UsuarioRequestDTO buscarUsuario(Long id);
     void salvaImagem(Long usuarioId, MultipartFile file);

     public void alteraSenha(AlteraSenhaDTO dto);

     UsuarioRequestFiliasDTO filtraFiliais(Long id);
}
