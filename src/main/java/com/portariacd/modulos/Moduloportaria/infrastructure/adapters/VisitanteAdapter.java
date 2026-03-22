package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.infrastructure.adapters.Modulos.VisitantePortariaSpec;
import com.portariacd.modulos.Moduloportaria.services.LogAcaoService;
import com.portariacd.modulos.Moduloportaria.domain.gateways.VisitanteGatewaysRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.visitante.StatusTypeDeleteVisitante;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.VisitanteDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.VisitanteFiltroDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.*;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class VisitanteAdapter implements VisitanteGatewaysRepository {
    private final VisitanteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final LogAcaoService service;
    private  final HistoryRepository historyRepository;
    private final RegistroVisitanteRepository repositoryRegistroPortatira;
    public VisitanteAdapter(VisitanteRepository visitanteRepository,
                            UsuarioRepository usuarioRepository,
                            LogAcaoService service,
                            RegistroVisitanteRepository registro,
                            HistoryRepository historyRepository
    ){
        this.repository = visitanteRepository;
        this.usuarioRepository = usuarioRepository;
        this.service = service;
        this.repositoryRegistroPortatira = registro;
        this.historyRepository = historyRepository;
    }
    @Override
    public Page<VisitanteDTO> listaVisitante(Pageable page, String busca,Integer filial) {
        System.out.println("minha busca "+busca);
     var spec =  Specification.allOf(
             VisitantePortariaSpec.filial(filial),
             VisitantePortariaSpec.busca(busca)
        );
        return repository.findAll(spec,page).map(VisitanteDTO::new);
    }
    @Override
    public Page<VisitanteFiltroDTO> listaVisitanteFiltro(Pageable page) {
        return repository.findAllByAtivoTrue(page).map(VisitanteFiltroDTO::new);
    }
    @Override
    @Transactional
    public String deleteRegistro(Long visitanteid, Long usuarioId, StatusTypeDeleteVisitante type) {
         var visitante = repository.findById(visitanteid).orElseThrow(
                  ()->new RuntimeException("Não foi possivel deletar visitante")
          );
      var usuario =  usuarioRepository.findById(usuarioId).orElseThrow(
                  ()->new RuntimeException("Não foi possivel deletar visitante")
          );
        var lista = repositoryRegistroPortatira.findAllByVisitante(visitante);
        var historyLista = historyRepository.findAllByVisitante(visitante);

        switch (type){
          case BLOQUEIO -> {
              if(!lista.isEmpty()) {
                  repositoryRegistroPortatira.deleteAll(lista);
              }
              visitante.setAtivo(false);
              visitante.setBloqueioAcesso(true);
              repository.save(visitante);
              salvaLog(new UsuarioRequestDTO(usuario),visitante,"BLOQUEIO_VISITANTE");
              return "Visitante Bloqueado";
          }
          case DESBLOQUEIO -> {
              salvaLog(new UsuarioRequestDTO(usuario),visitante,"DEBLOQEIO_VISITANTE");
              visitante.setAtivo(true);
              visitante.setBloqueioAcesso(false);
              repository.save(visitante);
              return "Visitante Desbloqueado";
          }
          case DELETE -> {
              if(!lista.isEmpty()) {
                  for (RegistroVisitantePortariaEntity h : lista) {

                      if (h.getEntradaVisitante() != null ) {
                          String url = h.getEntradaVisitante().getImagem();
                          String nomeArquivo = url.substring(url.lastIndexOf("/") + 1);
                          DelteImagem(url,"entrada");
                      }
                      if (h.getSaidaVisitante() != null ) {
                          String url = h.getSaidaVisitante().getImagem();
                          DelteImagem(url,"saida");
                      }
                  }
                  repositoryRegistroPortatira.deleteAll(lista);
              }
              if(!historyLista.isEmpty()){

                  historyRepository.deleteAll(historyLista);
              }
              if (visitante.getImagem() != null ) {
                  String url = visitante.getImagem();
                  DelteImagem(url,"avatar");

              }
              repository.delete(visitante);
              salvaLog(new UsuarioRequestDTO(usuario),visitante,"DELETE_VISITANTE");
              return "Visitante deletado";

          }
          default -> {
              throw  new RuntimeException("Opcão invalida!");

          }
      }
    }


    private void DelteImagem(String url,String direct){
        String nomeArquivo = url.substring(url.lastIndexOf("/") + 1);
        Path path = Paths.get(direct,nomeArquivo);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir imagem: " + path, e);
        }
    }

    private void salvaLog(UsuarioRequestDTO usuario, VisitanteEntity visitante, String acao){
        service.registrarLog(
                usuario,
                acao,
                String.format(
                        "Usuário %s fez uma acão no ID %d ",
                        usuario.nome(),
                        visitante.getId())
        );
    }
    @Override
    public VisitanteDTO buscaVisitante(Long visitanteId,String nome){
        Optional<VisitanteEntity>  visitanteOpt = repository.findFirstByIdOrNomeCompletoIgnoreCase(visitanteId,nome);
        VisitanteEntity visitante = visitanteOpt.orElse(null);
        if (visitante != null) {
            return new VisitanteDTO(visitante);
        } else {
           return null;
        }
//        var visitante = repository.findByIdByNome(visitanteId,nome);
//        if(visitante!=null){
//          return new VisitanteDTO(visitante);
//        }

    }
}
