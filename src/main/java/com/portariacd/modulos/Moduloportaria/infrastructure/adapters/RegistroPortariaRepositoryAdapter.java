package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;
import com.portariacd.modulos.Moduloportaria.services.LogAcaoService;
import com.portariacd.modulos.Moduloportaria.domain.gateways.RegistroPortariaGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.*;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.AtualizaRegistroPortariaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.UsuarioRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.*;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidaNomeImagem;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidaStatusPortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.history.HistoryEntrada;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.RegistroVisitantePortaria;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.history.HistoryEntradaEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.SaidaVisitanteEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.StatusTipoDeAcesso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Component
public class RegistroPortariaRepositoryAdapter implements RegistroPortariaGatewayRepository {
    @Value("${endpoint}")
    private String endpointUrl;
    private final RegistroVisitanteRepository repository;
    private final VisitanteRepository visitante;
    private final HistoryRepository history;
    private final UsuarioRepository usuarioRepository;
    private final ValidaStatusPortaria validaStatusPortaria;
    private final LogAcaoService service;
    private RecorrenciaRepository recorrenciaRepository;
    private static final Set<String> PERMISSOES_FULL_ACCESS = Set.of(
            "GERENCIAR_USUARIOS"
    );
    public RegistroPortariaRepositoryAdapter(
            RegistroVisitanteRepository repository
            ,VisitanteRepository visitante
            ,UsuarioRepository usuarioRepository
            ,ValidaStatusPortaria validaStatusPortaria,
            HistoryRepository history,
            LogAcaoService service,
            RecorrenciaRepository recorrencia
    ){
        this.repository = repository;
        this.visitante = visitante;
        this.usuarioRepository = usuarioRepository;
        this.validaStatusPortaria = validaStatusPortaria;
        this.history = history;
        this.service = service;
        this.recorrenciaRepository = recorrencia;
    }
    @Override
    public String registroPortaria(RegistroPortariaRequestDTO request, MultipartFile file){
      var visitanteEcontrado = visitante.findByOneByNumeroTelefone(request.getNumeroTelefone());
        String nameImagem = ValidaNomeImagem.criarDiretorio(file,"avatar",endpointUrl);;
        var rec = recorrenciaRepository.findByNome(request.getTipoAcesso().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Recorrência não encontrada: " + request.getTipoAcesso()));
        if(visitanteEcontrado.isEmpty()){
              var visitanteEntity = new VisitanteEntity(request,nameImagem);
            if("recorrente temporario".equals(request.getTipoAcesso())){
                if(request.getDataAcesso()==null){
                    throw  new RuntimeException("Informe uma data para continuar");
                }
                visitanteEntity.setDataRestritoAcesso(ConverteData(request.getDataAcesso()));
                visitanteEntity.setBloqueioAcesso(false);

            }
            visitanteEntity.setRecorrencia(rec);
            VisitanteEntity novoVisitante =  visitante.save(visitanteEntity);
            CadastroVisitante(request,novoVisitante);
              return "Entrada solicitada";
      }
        if(visitanteEcontrado.get()!=null){
            visitanteEcontrado.get().setImagem(nameImagem);
            visitanteEcontrado.get().setAtivo(true);
            visitanteEcontrado.get().setBloqueioAcesso(false);
            visitante.save(visitanteEcontrado.get());
        }
        visitanteEcontrado.ifPresent(v -> CadastroVisitante(request, v));
      return "Solictacao registrada com sucesso: aguardando entrada";

    }
    @Transactional
    public String registroPortariaTeste(RegistroPortariaRequestDTO request, MultipartFile file){
         visitante.findByOneByNumeroTelefone(request.getNumeroTelefone()).ifPresent(
                v->{
                    throw new RuntimeException("Existe um visitante com esse Numero Informado");
                }
        );
        var rec = recorrenciaRepository.findByNome(request.getTipoAcesso().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Recorrência não encontrada: " + request.getTipoAcesso()));
        String nameImagem = ValidaNomeImagem.criarDiretorio(file,"avatar",endpointUrl);;
        var visitanteEntity = new VisitanteEntity(request,nameImagem);
        if("recorrente temporario".equals(request.getTipoAcesso())){
                if(request.getDataAcesso()==null){
                    throw  new RuntimeException("Informe uma data para continuar");
                }
                visitanteEntity.setDataRestritoAcesso(ConverteData(request.getDataAcesso()));
                visitanteEntity.setBloqueioAcesso(false);
        }
        visitanteEntity.setRecorrencia(rec);
        VisitanteEntity novoVisitante =  visitante.save(visitanteEntity);
        CadastroVisitante(request,novoVisitante);
        return "Solictacão registrada com sucesso: aguardando entrada";

    }


    private String geraProtocolo(){
        Optional<RegistroVisitantePortariaEntity> ultimoRegistro = repository.findTopByOrderByProtocoloDesc();
        long proximoNumero = 1L;
        if (ultimoRegistro.isPresent()) {
            String ultimoProtocolo = ultimoRegistro.get().getProtocolo();
            try {
                // Remove zeros à esquerda e converte para número
                long numeroAtual = Long.parseLong(ultimoProtocolo);
                proximoNumero = numeroAtual + 1;
            } catch (NumberFormatException e) {
                System.err.println("Protocolo inválido encontrado: " + ultimoProtocolo);
                proximoNumero = 1L;
            }
        }
        return String.format("%06d", proximoNumero);
    }

    @Transactional
    private RegistroVisitantePortariaEntity CadastroVisitante(RegistroPortariaRequestDTO req, VisitanteEntity visitanteRequest){
        var usuario = usuarioRepository.findById(req.getCriadorId()).orElseThrow(
                ()->new RuntimeException("Erro ao buscar usuario!")
        );
        valid(req);
        var global = Boolean.parseBoolean(req.getGlobalAtivo());
        if(global){
            if(!visitanteRequest.getRecorrencia().getNome().equals("RECORRENTE")){
                throw new RuntimeException("Não foi possivel definir acesso 'Global' para acesso: "+StatusTipoDeAcesso.UNICO.name());
            }
            visitanteRequest.setAcessoGlobal(true);
        }
        RegistroVisitantePortariaEntity registro = new RegistroVisitantePortariaEntity(
                req,visitanteRequest,usuario
        );

        registro.setStatus(StatusPortaria.AGUARDANDO_ENTRADA);
        registro.setProtocolo(geraProtocolo());
       var resposta = repository.save(registro);
        var historyNovo = new HistoryEntrada(resposta);
        var historyEli = new HistoryEntradaEntity(historyNovo);
        history.save(historyEli);
       return resposta;

    }

    // valida nome da imagem caso exista retorna uma nova imagem
    private LocalDate ConverteData(String re){
        if(re == null || re.isBlank()){
            throw new RuntimeException("A data não pode está vazia!");
        }
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data =  LocalDate.parse(re, formatter);
        if(data.isBefore(LocalDate.now())){
            throw new RuntimeException("A data não pode ser menor que o dia atual!");
        }
        return data;
    }
    @Override
    @Transactional
    public Page<RequestPortariaDTO> listaPostaria(Pageable pageable, Integer filial,String busca) {
        Page<RequestPortariaDTO> page = repository.findAllByFilial(filial,pageable).map(entity->new RequestPortariaDTO(new RegistroVisitantePortaria(entity)));
        return page;
    }
    private void valid(RegistroPortariaRequestDTO req){
        recorrenciaRepository.findByNome(req.getTipoAcesso().toUpperCase()).orElseThrow(
                ()-> new RuntimeException("Não foi possivel encontrar a recorrencia")
        );
        // 2 — validar pedido ativo pela PLACA
        var registroPorPlaca = repository.findByPlacaVeiculoAndAtivo(req.getPlacaVeiculo());
        if (registroPorPlaca.isPresent()) {
            throw new RuntimeException("Já existe um pedido de entrada ativo para o Visitante informado");
        }
        var registroPorTelefone = repository.findByVisitanteNumeroTelefoneAndAtivo(req.getNumeroTelefone());
        if (registroPorTelefone.isPresent()) {
            throw new RuntimeException("Já existe um pedido de entrada ativo para o Visitante informado.");
        }
    }
//atualiza status para aguadando saida
    @Override
    @Transactional
    public StatusAtualizadoDTO atualizaEntrada(Long ficalId, long registroId, MultipartFile file) {
          UsuarioEntity usuarioFiscal =  usuarioRepository.findById(ficalId).orElseThrow(
                    ()->new RuntimeException("Usuario não existe ou estagio invalido")
            );
           RegistroVisitantePortariaEntity resposta =  repository.findById(registroId).orElseThrow(
                     ()-> new RuntimeException("Erro ao buscar registro, registro não encontrado!")
           );
           var visitante = resposta.getVisitante();
            if (Boolean.TRUE.equals(visitante.getBloqueioAcesso())) {
                System.out.println("acesso negado");
                throw new RuntimeException(
                        "Acesso de Visitante Bloqueado"
                );
            }
            RegistroVisitantePortaria re = new RegistroVisitantePortaria(resposta);
            validaStatusPortaria.validEntrada(re);
            String pasta = "entrada";
            String nomeUrl = ValidaNomeImagem.criarDiretorio(file,pasta,endpointUrl);
            // cria a entrada de motorista
            EntradaVisitanteEntity entradaVisitante = new EntradaVisitanteEntity();
            entradaVisitante.setDataEntrada(LocalDateTime.now());
            entradaVisitante.setFiscalEntradaId(usuarioFiscal.getId());
            entradaVisitante.setNomeFiscal(usuarioFiscal.getNome());
            entradaVisitante.setImagem(nomeUrl);
            resposta.setEntradaVisitante(entradaVisitante);
            // atualiza o status da entrada do motorista
            resposta.setStatus(StatusPortaria.AGUARDANDO_SAIDA);
         RegistroVisitantePortariaEntity respostaSalva =  repository.save(resposta);
        var historyEntrada = history.findByRegistroPortariaId(registroId);
        if(historyEntrada!=null){
            historyEntrada.UpdateHistoryInput(respostaSalva);
             history.save(historyEntrada);

         }
        salvaLog(new UsuarioRequestDTO(usuarioFiscal),respostaSalva,"UPDATE_ENTRADA_HISTORY");
        return new StatusAtualizadoDTO(respostaSalva,"Entrada Liberada!");


    }

    @Override
    public StatusAtualizadoDTO atualizaSaida(Long ficalId, long registroId,MultipartFile file) {
            UsuarioEntity usuarioFiscal =  usuarioRepository.findById(ficalId).orElseThrow(
                    ()->new RuntimeException("Usuario não existe ou estagio invalido")
            );
            RegistroVisitantePortariaEntity resposta =  repository.findById(registroId).orElseThrow(
                    ()-> new RuntimeException("Erro ao buscar registro, registro não encontrado!")
            );
            validaStatusPortaria.validSaida(new RegistroVisitantePortaria(resposta));
            String nomeUrl = ValidaNomeImagem.criarDiretorio(file,"saida",endpointUrl);
            var historyEntrada = history.findByRegistroPortariaId(registroId);
        // valida o statas
            // cria a entrada de motorista
            SaidaVisitanteEntity saidaVisitante = new SaidaVisitanteEntity();
            saidaVisitante.setDataSaida(LocalDateTime.now());
            saidaVisitante.setFicalSaidaId(usuarioFiscal.getId());
            saidaVisitante.setNomeFiscal(usuarioFiscal.getNome());
            resposta.setSaidaVisitante(saidaVisitante);
            VisitanteEntity visitanteEntity = visitante.findById(resposta.getVisitante().getId()).orElseThrow(
                    ()->new RuntimeException("Visitante não encontado")
            );

        boolean isRecorrente = visitanteEntity.getRecorrencia()!=null & "RECORRENTE".equals(visitanteEntity.getRecorrencia().getNome());
        boolean isRecorrenteTemporario =  visitanteEntity.getRecorrencia()!=null&& "RECORRENTE TEMPORARIO".equals(visitanteEntity.getRecorrencia().getNome());

        if(isRecorrente || isRecorrenteTemporario){
            if (Boolean.TRUE.equals(visitanteEntity.getBloqueioAcesso())){
                    throw new RuntimeException(
                            "Acesso proibido: recorrência temporária expirada!"
                    );

            }
            resposta.setStatus(StatusPortaria.SAIDA_LIBERADA);
            resposta.getSaidaVisitante().setImagem(nomeUrl);
            resposta.setAtivo(false);
            if (historyEntrada != null) {
                    // Atualiza o histórico com os
                // dados atuais da saída
                    historyEntrada.setImagemSaida(nomeUrl);
                   historyEntrada.setStatus(StatusPortaria.SAIDA_LIBERADA);
                    historyEntrada.UpdateHistoryOuput(resposta);
                    history.save(historyEntrada);
               }
              CadastroVisitante(new RegistroPortariaRequestDTO(resposta),visitanteEntity);

            }else {
                resposta.setStatus(StatusPortaria.SAIDA_LIBERADA);
               saidaVisitante.setImagem(nomeUrl);
               resposta.setAtivo(false);
                if(historyEntrada!=null){
                    historyEntrada.UpdateHistoryOuput(resposta);
                    historyEntrada.setStatus(StatusPortaria.SAIDA_LIBERADA);
                    historyEntrada.setImagemSaida(resposta.getSaidaVisitante().getImagem());
                    history.save(historyEntrada);
                }
            }
            // atualiza o status da entrada do motorista
            RegistroVisitantePortariaEntity respostaSalva =  repository.save(resposta);
        salvaLog(new UsuarioRequestDTO(usuarioFiscal),respostaSalva,"UPDATE_SAIDA_HISTORY");
        return new StatusAtualizadoDTO(respostaSalva,"Saida Liberada!");
    }
    @Override
    @Transactional
    public Page<RequestPortariaDTO> solitacaoUsuario(Long usuarioID,Pageable page,String busca) {
        Page<RequestPortariaDTO> pageEncontado;
        if(busca!=null && !busca.isEmpty()) {
            pageEncontado =   repository.registrosUsuarios(page, usuarioID,busca).map(
                    RequestPortariaDTO::new);
        }else{
            pageEncontado =  repository.findAllByCriador_idOrderByDataCriacaoDesc(page, usuarioID).map(
                    RequestPortariaDTO::new);
        }
        return pageEncontado;
    }

    @Override
    @Transactional
    public Map<String, List<RequestPortariaDTO>> FindAllStatus(String Status) {
        Map<String,List<RequestPortariaDTO>> listMap = new HashMap<>();
        List<RequestPortariaDTO> solicitacao = repository.findAllByStatus(StatusPortaria.buscaStatus(Status)).stream().map(
                RequestPortariaDTO::new
        ).toList();
        listMap.put("registros",solicitacao);
        return listMap;
    }

    @Override
    @Transactional
    public Page<RequestPortariaDTO> listaPendentes(Pageable pageable, Integer filial,String busca,String status) {
        Page<RequestPortariaDTO> page;
        if (busca != null && !busca.isEmpty()) {
            page = repository.findAllByFilialAndBusca(filial, pageable, busca).map(entity -> new RequestPortariaDTO(new RegistroVisitantePortaria(entity)));
        } else if (status!=null && !status.isEmpty()) {
            page = repository.findAllByFilialAndBuscaStatusSalvo(filial, pageable, StatusPortaria.buscaStatus(status)).map(entity -> new RequestPortariaDTO(new RegistroVisitantePortaria(entity)));

        } else{
            page = repository.findAllByFilialStatus(filial, pageable, StatusPortaria.AGUARDANDO_ENTRADA).map(entity -> new RequestPortariaDTO(new RegistroVisitantePortaria(entity)));
        }
       return  page;
    }

    @Override
    @Transactional
    public Page<RequestPortariaDTO> FindAllPortarias(Pageable pageable, Integer filial,String busca,Boolean ativo) {
        Page<RequestPortariaDTO> page;
        var spec = Specification.allOf(
                RegistroPortariaSpec.filial(filial),
                RegistroPortariaSpec.busca(busca),
                RegistroPortariaSpec.ativoRegistro(ativo)
                );
        return   repository.findAll(spec,pageable).map(entity -> new RequestPortariaDTO(new RegistroVisitantePortaria(entity)));
    }

    @Override
    @Transactional
    public String registroPortariaRequest(CadastroTypeFacture data, MultipartFile file) {
        if(data instanceof AtualizaRegistroPortariaDTO request){
            VisitanteEntity v = visitante.findById(request.getVisitanteId()).orElseThrow(
                    ()->new RuntimeException("Visitante não encontrado")
            );
           if(v.getBloqueioAcesso()){
             throw new RuntimeException("Visitante bloqueado");
           }
            var rec = recorrenciaRepository.findByNome(request.getTipoAcesso().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Recorrência não encontrada: " + request.getTipoAcesso()));
            v.setRecorrencia(rec);
            visitante.save(v);
            CadastroVisitante(new RegistroPortariaRequestDTO(request,v),v);
           return "Visitante atualizado";
        }
        if(data instanceof RegistroPortariaRequestDTO r){
            if(file!=null){
               return registroPortariaTeste(r,file);
            }
            else {
                throw new RuntimeException("Informa uma imagem para continuar");
            }
        }
         return  null;
    }

    @Override
    public RequestPortariaDTO visulizarRegistro(Long registro) {
        RequestPortariaDTO response = repository.findById(registro).map(RequestPortariaDTO::new).orElseThrow(
                ()->new RuntimeException("Registro não encontrado!")
        );
        return response;
    }
    @Override
    public void deleteRegistroPortaria(Long registroId,Long usuarioId) {
        var usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                ()->new RuntimeException("Não foi possivel encontrar usuario: "+usuarioId)
        );

       var registro = repository.findById(registroId).orElseThrow(
                ()-> new RuntimeException("Sem registro Encontrado")
        );
       if(registro.getStatus().equals(StatusPortaria.AGUARDANDO_SAIDA)){
           throw new RuntimeException("Não foi possivel deletar o status: atualizar para "+StatusPortaria.SAIDA_LIBERADA);
       }
        if (registro.getEntradaVisitante() != null ) {
            String url = registro.getEntradaVisitante().getImagem();
            String nomeArquivo = url.substring(url.lastIndexOf("/") + 1);
            DelteImagem(url,"entrada");
        }
        if (registro.getSaidaVisitante() != null ) {
            String url = registro.getSaidaVisitante().getImagem();
            DelteImagem(url,"saida");
        }
        salvaLog(new UsuarioRequestDTO(usuario),registro,"DELETE_ENTRADA_VISITANTE");
        repository.delete(registro);
    }

    @Override
    @Transactional
    public void atualizaRegistro(AtualizaRegistro update) {
        RegistroVisitantePortariaEntity registro = repository.findById(update.id()).orElseThrow(
                ()-> new RuntimeException("Registro nao encontrado")
        );
        if(registro.getStatus().equals(StatusPortaria.AGUARDANDO_SAIDA)
                || registro.getStatus().equals(StatusPortaria.SAIDA_LIBERADA)
                || !registro.getAtivo() || registro.getStatus().equals(StatusPortaria.FECHADO_AUTOMATICO)
        ){
            throw new RuntimeException("Não e possivel atualizar a solicitação");
        }
        var recorrencia = recorrenciaRepository.findByNome(update.tipoDeAcesso().toUpperCase()).orElseThrow();

        registro.atualizarEntrada(update);
        registro.getVisitante().setNumeroTelefone(update.numeroTelefone());
        registro.getVisitante().setNomeCompleto(update.nomeCompleto());

        if(update.tipoDeAcesso().equals("RECORRENTE TEMPORARIO")){
            LocalDate dataAcesso = ConverteData(update.dataAcesso());

            registro.getVisitante().setDataRestritoAcesso(dataAcesso);
            registro.getVisitante().setBloqueioAcesso(false);
        }
        else{

            registro.getVisitante().setDataRestritoAcesso(null);

    }
        registro.getVisitante().setTipoPessoa(update.tipoPessoa());
        registro.getVisitante().setOcupacao(update.tipoPessoa());
//        registro.getVisitante().setTipoAcesso(StatusTipoDeAcesso.StatusAdd(update.tipoDeAcesso().toLowerCase()));
        registro.getVisitante().setRecorrencia(recorrencia);
        repository.save(registro);




    }

    ;
    private void salvaLog(UsuarioRequestDTO usuario, RegistroVisitantePortariaEntity registroVisitantePortaria,String acao){
       service.registrarLog(
               usuario,
               acao,
               String.format(
                       "Usuário %s fez uma acão no ID %d ",
                       usuario.nome(),
                       registroVisitantePortaria.getId())
       );
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

}
