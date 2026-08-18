package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.*;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.*;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.armario.ArmarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ChaveDevolucao;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.DesvolucaoChaveDto;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.EntregaChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.EntregaChavesResponseDetalhesDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.ArmarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.BlocoChavesRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.exeption.UsuarioConsumerValidation;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method.DesvolucaoChaveUsuarioDto;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method.FactureMetodChave;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaChavesCDService {
    @Autowired
    private EntregaChaveRepository repository;
    @Autowired
    private UsuarioConsumerRepository usuarioConsumerRepository;
    @Autowired
    private ArmarioRepository armarioRepository;
    @Autowired
    private BlocoChavesRepository chavesRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EntregaChaveHistoryRepository entregaChaveHistoryRepository;

    public DevolucaoInteface entregaDeChaves(EntregaChavesDTO d){
        var armario = armarioRepository.findById(d.armarioId())
                .orElseThrow(
                        ()->new RuntimeException("Armario não encontrado")
                );
        var chave = armario.getBlocoChaves().stream()
                .filter(c -> c.getNumero().equals(d.numeroDaChave()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));
        var entregaAtiva = repository.findEntregaAtiva(chave.getId());

        if (entregaAtiva.isPresent()) {
            throw new RuntimeException(
                    "Chave já está em uso pelo colaborador: " +
                            entregaAtiva.get().getNomeColaborador()
            );
        }
//        var usuarioConsumer = usuarioConsumerRepository.buscaUsuario(d.gmIDMatricula());
        var usuarioConsumer = usuarioConsumerRepository.findById(d.usuarioConsumerId());

        if (!usuarioConsumer.isPresent()){
            throw new UsuarioConsumerValidation("Colaborador não cadastrado!",false);
        }
        if(Long.parseLong(usuarioConsumer.get().getFilial().toString())!=armario.getFilial()){
            throw new UsuarioConsumerValidation("Colaborador Diferente da filial!",false);
        }
        var usuarioEntrega =  usuarioRepository.findById(d.usuarioId()).orElseThrow(
                ()->new UsuarioConsumerValidation("Usuario não encontrado",false)
        );
        var contemChaves = repository.findEntregaUsuario(usuarioConsumer.get().getMatricula(),armario.getId());
        if(contemChaves.isPresent()){
            System.out.println();
            if (contemChaves.get().getBlocoChaves().getArmario().getId() == chave.getArmario().getId()){
                var smd = """
                        Chave ocupada!
                        Usuario: %s
                        chave: %d
                        """.formatted(contemChaves.get().getBlocoChaves().getUsuarioOcupacao(),contemChaves.get().getBlocoChaves().getNumero());
                throw new RuntimeException(
                        smd
                );
            }
        }
        if (!chave.isDisponivel()) {
            throw new RuntimeException("Chave já está em uso");
        }
        chave.setDisponivel(false);
        chave.setStatus(StatusArmario.OCUPADO);
        chave.setUsuarioOcupacao(usuarioConsumer.get().getNome());
        chave.setUsuarioOcupacaoId(usuarioConsumer.get().getId());
        var entrega = new EntregaChave();
        entrega.setMatriculaColaborador(usuarioConsumer.get().getMatricula());
        entrega.setNomeColaborador(usuarioConsumer.get().getNome());
        entrega.setUsuarioPortariaRetirada(usuarioEntrega.getUsername());
        entrega.setUsuarioIdRetirada(usuarioConsumer.get().getId());
        entrega.setDataHoraRetirada(OffsetDateTime.now());
        entrega.setFilialId(armario.getFilial());
        entrega.setEntregue(false);
        entrega.setBlocoChaves(chave);
        var history = new EntregaChaveHistory();
        var matircula = usuarioConsumer.get().getMatricula()!=null?usuarioConsumer.get().getMatricula():usuarioConsumer.get().getCpf();
        history.setMatriculaColaborador(matircula);
        history.setEmpresa(usuarioConsumer.get().getEmpresa());
        history.setNomeColaborador(usuarioConsumer.get().getNome());
        history.setUsuarioPortariaRetirada(usuarioEntrega.getUsername());
        history.setUsuarioIdRetirada(usuarioConsumer.get().getId());
        history.setDataHoraRetirada(OffsetDateTime.now());
        history.setFilialId(armario.getFilial());
        history.setBlocoChaves(chave.getId());
        history.setStatus(StatusArmario.OCUPADO.name());
        repository.save(entrega);
        chavesRepository.save(chave);
        entregaChaveHistoryRepository.save(history);
        var s =new  EntregaToken();
        s.setMsg("Entregue com sucesso");
        s.setType("Emtrega");
        return s;

    }


//    public String entregaDeChaves(EntregaChavesDTO d){
//        var armario = armarioRepository.findById(d.armarioId())
//                .orElseThrow(
//                        ()->new RuntimeException("Armario não encontrado")
//                );
//
//        var chave = armario.getBlocoChaves().stream()
//                .filter(c -> c.getNumero().equals(d.numeroDaChave()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));
//        var entregaAtiva = repository.findEntregaAtiva(chave.getId());
//
//        if (entregaAtiva.isPresent()) {
//            throw new RuntimeException(
//                    "Chave já está em uso pelo colaborador: " +
//                            entregaAtiva.get().getNomeColaborador()
//            );
//        }
//        var usuarioConsumer = usuarioConsumerRepository.buscaUsuario(d.gmIDMatricula());
//        if (!usuarioConsumer.isPresent()){
//            throw new RuntimeException("Colaborador não cadastrado!");
//        }
//      var usuarioEntrega =  usuarioRepository.findById(d.usuarioId()).orElseThrow(
//              ()->new RuntimeException("Usuario não encontrada")
//      );
//        var contemChaves = repository.findEntregaUsuario(usuarioConsumer.get().getMatricula());
//         if(contemChaves.isPresent()){
//             throw new RuntimeException(
//                     "Colaborador com chave Ativa no usuario!"
//             );
//         }
//        if (!chave.isDisponivel()) {
//            throw new RuntimeException("Chave já está em uso");
//        }
//        chave.setDisponivel(false);
//        chave.setStatus(StatusArmario.OCUPADO);
//       var entrega = new EntregaChave();
//       entrega.setMatriculaColaborador(usuarioConsumer.get().getMatricula());
//       entrega.setNomeColaborador(usuarioConsumer.get().getNome());
//       entrega.setUsuarioPortariaRetirada(usuarioEntrega.getUsername());
//       entrega.setUsuarioIdRetirada(usuarioEntrega.getId());
//       entrega.setDataHoraRetirada(OffsetDateTime.now());
//       entrega.setFilialId(armario.getFilial());
//       entrega.setBlocoChaves(chave);
//        repository.save(entrega);
//        chavesRepository.save(chave);
//        return "d";
//
//    }

    public DevolucaoInteface liberacaoDeChaves(DesvolucaoChaveDto item){
        var armario = armarioRepository.findById(item.item().arm())
                .orElseThrow(
                        ()->new RuntimeException("Armario não encontrado")
                );
        var usuarioEntrega =  usuarioRepository.findById(item.usuarioId()).orElseThrow(
                ()->new UsuarioConsumerValidation("Usuario não encontrado",false)
        );

        var chave = armario.getBlocoChaves().stream()
                .filter(c -> c.getNumero().equals(item.item().chave()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));
        var entregaAtiva = repository.findEntregaAtiva(chave.getId());

        if (entregaAtiva.isPresent()) {
            chave.setStatus(StatusArmario.LIVRE);
            chave.setDisponivel(true);
            chave.setUsuarioOcupacao(null);
            chave.setUsuarioOcupacaoId(null);
            var history = new EntregaChaveHistory();
            history.setUsuarioPortariaRetirada(usuarioEntrega.getUsername());
            history.setUsuarioIdRetirada(chave.getUsuarioOcupacaoId());
            history.setDataHoraRetirada(OffsetDateTime.now());
            history.setFilialId(armario.getFilial());
            history.setBlocoChaves(chave.getId());
            history.setStatus(StatusArmario.LIVRE.name());

            entregaAtiva.get().setDataHoraDevolucao(LocalDateTime.now());
            entregaAtiva.get().setUsuarioPortariaDevolucao(usuarioEntrega.getNome());
            entregaAtiva.get().setUsuarioIdDevolucao(usuarioEntrega.getId());
            entregaAtiva.get().setEntregue(true);
            repository.save(entregaAtiva.get());
            chavesRepository.save(chave);
            entregaChaveHistoryRepository.save(history);
            var e = new DevolucaoChaves();
            e.setType("Devolucao");
            e.setMsg("Devolvida com sucesso");
            return e ;
        }
        throw new RuntimeException(
                "Chave entregue,\nInforme chave em devolução"

        );

    }



    public List<ArmarioResponseDTO> listaDeArmariosCd(Integer filial){
        return armarioRepository.findAllFilial(filial).stream().map(ArmarioResponseDTO::new).toList();
    }

    public List<EntregaChavesResponseDTO> TresUlmicasRetiradas(Long filial,Long id){
        return repository.findUltimo50PorArmarioDaFilial(filial,id).stream().map(EntregaChavesResponseDTO::new).toList();
    }

    public EntregaChavesResponseDetalhesDTO detalhesChaves(Long filial, Long arm, Integer chave) {

        var itens = chavesRepository.findChavaDetalhesFilial(filial, arm, chave);

        if (itens == null) {
           return null ;
        }

        UsuarioConsumerChaves usuario = null;

        if (itens.getUsuarioOcupacaoId() != null) {
            usuario = usuarioConsumerRepository
                    .findById(itens.getUsuarioOcupacaoId())
                    .orElse(null);
        }

        return new EntregaChavesResponseDetalhesDTO(itens, usuario);
    }

    public String liberacaoDeChavesFacture(@Valid FactureMetodChave a) {

        if(a instanceof DesvolucaoChaveUsuarioDto s){
            System.out.println("Instancei a de devolucao Chave usasurio");
        }
        return "";
    }

    public List<EntregaChavesResponseDetalhesDTO> ocupadoFilial(Long filial, Long arm) {
       var resposta = chavesRepository.findChavaOcupadoFilial(filial,arm);
        if (resposta == null) {
            return null ;
        }
        return resposta.stream().map(e->{
            UsuarioConsumerChaves usuario = null;

            if (e.getUsuarioOcupacaoId() != null) {
                usuario = usuarioConsumerRepository
                        .findById(e.getUsuarioOcupacaoId())
                        .orElse(null);
            }
         return  new EntregaChavesResponseDetalhesDTO(e,usuario);
        }).toList();
    }

    public DevolucaoInteface devolverChavePorToken(@Valid EntregaChavesDTO dto) {
        var armario = armarioRepository.findById(dto.armarioId())
                .orElseThrow(
                        ()->new RuntimeException("Armario não encontrado")
                );
        var chave = armario.getBlocoChaves().stream()
                .filter(c -> c.getNumero().equals(dto.numeroDaChave()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chave não encontrada"));
        var entregaAtiva = repository.findEntregaAtiva(chave.getId());
        if(entregaAtiva.isPresent()){
            liberacaoDeChaves(new DesvolucaoChaveDto(dto.usuarioId(),new ChaveDevolucao(chave.getNumero(),armario.getId())));
            var e = new DevolucaoChaves();
            e.setType("Devolucao");
            e.setMsg("Devolvida com sucesso");
            return e;
        }else{
            entregaDeChaves(dto);
            var s =new  EntregaToken();
            s.setMsg("Entregue com sucesso");
            s.setType("Entrega");
          return s;
        }
    }
}
