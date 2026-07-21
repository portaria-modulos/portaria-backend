package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BiometriaFacial;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BiometriaRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioProjection;
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
}
