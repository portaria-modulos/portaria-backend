package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.DevolucaoInteface;
import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.EntregaToken;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.*;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioConsumerService {
    @Autowired
    private UsuarioConsumerRepository repository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BiometriaService service;
    @Autowired
    private CriptografiaService criptografiaService;
    @Autowired
    private BiometriaRepository biometriaRepository;
    @Autowired
    private EntregaChaveRepository entregaChaveRepository;
    public Map<String, String> cadastroDeUsuario(UsuarioConsumerRequestDTO usm, MultipartFile file) throws Exception {
        var usuario = repository.findByUsuario(usm.GmcoreId(),usm.matricula());
        if(usuario.isPresent()){

            if(!usuario.get().getAtivo()){
                throw new RuntimeException("Usuario bloqueado");
            }

            throw new RuntimeException("Colaborador ja cadastrado");
        }

        var biometria = service.extrairEmbedding(file);
        System.out.println("chegou aqui "+biometria.length);

        if(biometria!=null) {
            var usuarioChaves = new UsuarioConsumerChaves(usm);
            BiometriaFacial biometriaFacial = new BiometriaFacial(usuarioChaves, biometria, OffsetDateTime.now());
            usuarioChaves.setBiometriaFacial(biometriaFacial);
            repository.save(usuarioChaves);
        }

     return Map.of("msg","Usuario criado com sucesso");
    }
    public List<UsuarioConsumerResponseDTO> lista() {
       return repository.findAllUsuario().stream().map(UsuarioConsumerResponseDTO::new).toList();
    }
    public UsuarioProjection extrairEmbedding(MultipartFile file) {
        var biometria = service.extrairEmbeddingFace(file);
        String vetor = Arrays.toString(biometria)
                .replace(" ", "");
        var usuario =  repository.buscarUsuarioPelaBiometria(vetor);
        if(usuario==null){
            throw new RuntimeException("Usuario não encontrado");
        }
       return usuario;
    }


    @Transactional
    public DevolucaoInteface deleteUsuario(Long idusuario) {

        var usuario = repository.findById(idusuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var usuarioComChavesAtivoNoUsuario = entregaChaveRepository.entregaChaveUsuarioAtivoFalse(idusuario);

        if (usuarioComChavesAtivoNoUsuario.isEmpty()) {

            Long idBio = (usuario.getBiometriaFacial() != null) ? usuario.getBiometriaFacial().getId() : null;

            // 1. Remove a referência na tabela de usuário usando o id do usuário
            entityManager.createNativeQuery("UPDATE usuario_consumer_chaves SET biometria_facial_id = NULL WHERE id = :idUsuario")
                    .setParameter("idUsuario", idusuario)
                    .executeUpdate();

            // 2. Deleta a biometria pelo ID correto dela (SQL puro)
            if (idBio != null) {
                entityManager.createNativeQuery("DELETE FROM biometria_facial WHERE id = :idBio")
                        .setParameter("idBio", idBio)
                        .executeUpdate();
            }

            // 3. Limpa o cache do Hibernate para ele não tentar buscar a biometria apagada
            entityManager.clear();

            // 4. Deleta o usuário de forma limpa pelo ID
            repository.deleteById(idusuario);
            repository.flush();

            var s = new EntregaToken();
            s.setMsg("Usuario deletado");
            s.setType("Entrega");
            return s;
        }

        String valoresChaves = usuarioComChavesAtivoNoUsuario.stream()
                .map(e -> e.getBlocoChaves().getNumero())
                .distinct()
                .toList().toString();

        String mensagem = """
        Erro ao deletar usuario:
        Usuario com chave(s) ativa(s): %s
        """.formatted(valoresChaves);

        throw new RuntimeException(mensagem);
    }
}
