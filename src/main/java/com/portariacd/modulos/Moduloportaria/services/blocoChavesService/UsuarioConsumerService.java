package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.DevolucaoInteface;
import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.EntregaToken;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.*;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.FactoryResponseChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.TerceirizadoResponse;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerUpdateDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.FilialRepository;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
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
    @Autowired
    private FilialRepository filialRepository;
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
    @Autowired
    private AuditoriaChavesService auditoriaChavesService;

    @Transactional
    public Map<String, String> cadastroDeUsuario(FactoryResponseChaves create, MultipartFile file) throws Exception {
        validarFilial(create.getFilial());
        var s = service.extrairEmbeddingFace(file);
        String vetor = Arrays.toString(s)
                .replace(" ", "");
        var us =  repository.buscarUsuarioPelaBiometria(vetor);
       if(us!=null){
           throw new RuntimeException("Facial ja pertence ao Usuario: "+us.getNome());
       }
        if(create instanceof UsuarioConsumerRequestDTO usm){
            var usuario = repository.findByUsuario(usm.getGmId(),usm.getMatricula());
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
                var salvo = repository.save(usuarioChaves);
                registrarCriacaoUsuarioConsumer(salvo);
            }

            return Map.of("msg","Usuario criado com sucesso");
        }
        if(create instanceof TerceirizadoResponse terceirizadoResponse){
            var usuario = repository.findByUsuarioCpf(terceirizadoResponse.getCpf());
            if(usuario.isPresent()){

                if(!usuario.get().getAtivo()){
                    throw new RuntimeException("Usuario bloqueado");
                }

                throw new RuntimeException("Colaborador ja cadastrado");
            }

            var biometria = service.extrairEmbedding(file);
            System.out.println("chegou aqui "+biometria.length);

            if(biometria!=null) {
                var usuarioChaves = new UsuarioConsumerChaves(terceirizadoResponse);
                BiometriaFacial biometriaFacial = new BiometriaFacial(usuarioChaves, biometria, OffsetDateTime.now());
                usuarioChaves.setBiometriaFacial(biometriaFacial);
                var salvo = repository.save(usuarioChaves);
                registrarCriacaoUsuarioConsumer(salvo);
            }

            return Map.of("msg","Usuario criado com sucesso");
        }
        throw new RuntimeException("Erro ao buscar usuario");
    }

    public List<UsuarioConsumerResponseDTO> lista(Integer filial) {
       return repository.findAllByFilial(filial).stream().map(UsuarioConsumerResponseDTO::new).toList();
    }

    @Transactional
    public void cadastroDeUsuarioSemBiometria(FactoryResponseChaves create) {
        validarFilial(create.getFilial());
        if (create instanceof UsuarioConsumerRequestDTO usm) {
            var usuario = repository.findByUsuario(usm.getGmId(), usm.getMatricula());
            if (usuario.isPresent()) {
                if (!usuario.get().getAtivo()) throw new RuntimeException("Usuario bloqueado");
                throw new RuntimeException("Colaborador ja cadastrado");
            }
            var salvo = repository.save(new UsuarioConsumerChaves(usm));
            registrarCriacaoUsuarioConsumer(salvo);
            return;
        }
        if (create instanceof TerceirizadoResponse terceirizado) {
            var usuario = repository.findByUsuarioCpf(terceirizado.getCpf());
            if (usuario.isPresent()) {
                if (!usuario.get().getAtivo()) throw new RuntimeException("Usuario bloqueado");
                throw new RuntimeException("Colaborador ja cadastrado");
            }
            var salvo = repository.save(new UsuarioConsumerChaves(terceirizado));
            registrarCriacaoUsuarioConsumer(salvo);
            return;
        }
        throw new IllegalArgumentException("Tipo de usuário inválido");
    }

    private void validarFilial(Integer numeroFilial) {
        var filial = filialRepository.findByNumeroFilial(numeroFilial)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Filial não cadastrada: " + numeroFilial));

        if (!Boolean.TRUE.equals(filial.getAtivo())) {
            throw new IllegalArgumentException("Filial inativa: " + numeroFilial);
        }
    }

    @Transactional
    public Map<String, String> atualizaUsuario(Long usuarioId, UsuarioConsumerUpdateDTO update,
                                                MultipartFile file) {
        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        var snapshotAnterior = auditoriaChavesService.snapshotUsuarioConsumer(usuario);

        if (temValor(update.getMatricula())) {
            validarIdentificador(repository.findByMatricula(update.getMatricula()), usuario,
                    "matrícula");
            usuario.setMatricula(update.getMatricula());
        }
        if (temValor(update.getGmId())) {
            validarIdentificador(repository.findByGmcoreId(update.getGmId()), usuario,
                    "GM ID");
            usuario.setGmcoreId(update.getGmId());
        }
        if (temValor(update.getCpf())) {
            validarIdentificador(repository.findByCpf(update.getCpf()), usuario, "CPF");
            usuario.setCpf(update.getCpf());
        }
        if (temValor(update.getNome())) usuario.setNome(update.getNome());
        if (temValor(update.getSetor())) usuario.setSetor(update.getSetor());
        if (update.getFilial() != null) usuario.setFilial(update.getFilial());
        if (temValor(update.getNomeEmpresa())) usuario.setEmpresa(update.getNomeEmpresa());
        if (temValor(update.getTipoFuncionario())) {
            String tipoFuncionario = update.getTipoFuncionario().toLowerCase();
            usuario.setTipoFuncionario(tipoFuncionario);
            usuario.setTipoColaboradorFuncionario(tipoFuncionario.equals("funcionario"));
        }
        if (update.getUsuarioInsert() != null) usuario.setUsuarioInsert(update.getUsuarioInsert());

        if (file != null && !file.isEmpty()) {
            var embedding = service.extrairEmbeddingFace(file);
            String vetor = Arrays.toString(embedding).replace(" ", "");
            var usuarioComMesmaFace = repository.buscarUsuarioPelaBiometria(vetor);
            if (usuarioComMesmaFace != null && usuarioComMesmaFace.getId() != usuarioId) {
                throw new RuntimeException("Facial já pertence ao usuário: "
                        + usuarioComMesmaFace.getNome());
            }

            if (usuario.getBiometriaFacial() == null) {
                usuario.setBiometriaFacial(new BiometriaFacial(usuario, embedding, OffsetDateTime.now()));
            } else {
                usuario.getBiometriaFacial().setEmbedding(embedding);
                usuario.getBiometriaFacial().setDataCadastro(OffsetDateTime.now());
            }
        }

        var salvo = repository.save(usuario);
        auditoriaChavesService.registrar(
                AcaoAuditoriaChaves.EDITAR,
                ModuloAuditoriaChaves.USUARIO,
                "UsuarioConsumerChaves",
                salvo.getId(),
                "Usuário de chaves %s atualizado.".formatted(salvo.getNome()),
                salvo.getFilial() == null ? null : salvo.getFilial().longValue(),
                null,
                null,
                null,
                snapshotAnterior,
                auditoriaChavesService.snapshotUsuarioConsumer(salvo),
                null,
                null
        );
        return Map.of("msg", "Usuário atualizado com sucesso");
    }

    private boolean temValor(String valor) {
        return valor != null && !valor.isBlank();
    }

    private void validarIdentificador(java.util.Optional<UsuarioConsumerChaves> encontrado,
                                      UsuarioConsumerChaves atual, String campo) {
        if (encontrado.isPresent() && encontrado.get().getId() != atual.getId()) {
            throw new RuntimeException("Já existe um usuário com a " + campo + " informada");
        }
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
        var snapshotAnterior = auditoriaChavesService.snapshotUsuarioConsumer(usuario);

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

            auditoriaChavesService.registrar(
                    AcaoAuditoriaChaves.EXCLUIR,
                    ModuloAuditoriaChaves.USUARIO,
                    "UsuarioConsumerChaves",
                    idusuario,
                    "Usuário de chaves %s excluído.".formatted(usuario.getNome()),
                    usuario.getFilial() == null ? null : usuario.getFilial().longValue(),
                    null,
                    null,
                    null,
                    snapshotAnterior,
                    null,
                    null,
                    null
            );

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

    private void registrarCriacaoUsuarioConsumer(UsuarioConsumerChaves usuario) {
        auditoriaChavesService.registrar(
                AcaoAuditoriaChaves.CRIAR,
                ModuloAuditoriaChaves.USUARIO,
                "UsuarioConsumerChaves",
                usuario.getId(),
                "Usuário de chaves %s criado.".formatted(usuario.getNome()),
                usuario.getFilial() == null ? null : usuario.getFilial().longValue(),
                null,
                null,
                null,
                null,
                auditoriaChavesService.snapshotUsuarioConsumer(usuario),
                null,
                null
        );
    }
}
