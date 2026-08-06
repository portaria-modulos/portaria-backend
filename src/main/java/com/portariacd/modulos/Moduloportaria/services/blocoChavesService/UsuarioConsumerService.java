package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.DevolucaoInteface;
import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.EntregaToken;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.*;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioConsumerService {
    @Autowired
    private UsuarioConsumerRepository repository;
    @Autowired
    private BiometriaService service;
    @Autowired
    private CriptografiaService criptografiaService;
    @Autowired
    private BiometriaRepository biometriaRepository;
    @Autowired
    private EntregaChaveRepository entregaChaveRepository;
    public Map<String, String> cadastroDeUsuario(UsuarioConsumerRequestDTO usm) throws Exception {
        var usuario = repository.findByUsuario(usm.GmcoreId(),usm.matricula());
        if(usuario.isPresent()){

            if(!usuario.get().getAtivo()){
                throw new RuntimeException("Usuario bloqueado");
            }

            throw new RuntimeException("Colaborador ja cadastrado");
        }

        var biometria = service.extrairEmbedding(usm.imagemFacial());
        if(biometria!=null) {
            var usuarioChaves = new UsuarioConsumerChaves(usm);
            BiometriaFacial biometriaFacial = new BiometriaFacial(usuarioChaves, biometria, OffsetDateTime.now());
            usuarioChaves.setBiometriaFacial(biometriaFacial);
            repository.save(usuarioChaves);
        }

     return Map.of("msg","Usuario criado com sucesso");
    }
    public List<UsuarioConsumerRequestDTO> lista() {
       return repository.findAllUsuario().stream().map(UsuarioConsumerRequestDTO::new).toList();
    }
    public UsuarioProjection extrairEmbedding(@NotBlank String s) {
        var biometria = service.extrairEmbeddingFace(s);
        String vetor = Arrays.toString(biometria)
                .replace(" ", "");

        System.out.println("eb "+vetor);
        var usuario =  repository.buscarUsuarioPelaBiometria(vetor);
        if(usuario==null){
            throw new RuntimeException("Usuario não encontrado");
        }
       return usuario;
    }
    public DevolucaoInteface deleteUsuario(Long idusuario) {

        var usuario =  repository.findById(idusuario).orElseThrow(()->new RuntimeException("Usuario não entrado"));
        if(usuario==null){
            throw new RuntimeException("Usuario não encontrado");
        }
       var usuarioComChavesAtivoNoUsuario = entregaChaveRepository.entregaChaveUsuarioAtivoFalse(idusuario);
        if(usuarioComChavesAtivoNoUsuario.isPresent()){
            String mensagem  = """
                    Erro ao deletar usuario:\n
                    Usuario com chave ativa: %s
                    """.formatted(usuarioComChavesAtivoNoUsuario.get().getBlocoChaves().getNumero());
            throw new RuntimeException(mensagem);
        }

        repository.delete(usuario);
        var s =new EntregaToken();
          s.setMsg("Usuario deletado");
           s.setType("Entrega");
           return s;
    }
}
