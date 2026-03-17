package com.portariacd.modulos.Moduloportaria.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portariacd.modulos.Moduloportaria.services.CadastroPortariaService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.*;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.config.ConverteJson;
import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@RequestMapping("portaria/v1")
public class PortariaRegistroControler {
    private final CadastroPortariaService service;
    private Validator validator;
    private ConverteJson converteJson;
    public  PortariaRegistroControler(CadastroPortariaService service,ConverteJson converteJson,Validator valid){
        this.service = service;
        this.validator = valid;
        this.converteJson = converteJson;
    }
    @PostMapping("/cadastro")
    @Transactional
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'CRIAR_REGISTRO')")
    public ResponseEntity<Map<String, String>> registro(@RequestParam("data")  String data,
                                                        @RequestParam("file") MultipartFile file,
                                                        UriComponentsBuilder builder) throws IOException {
        try {
             var conver = converteJson.conversor(data, RegistroPortariaRequestDTO.class);
            String resposta = service.cadastro(conver, file);
            Set<ConstraintViolation<RegistroPortariaRequestDTO>> violations = validator.validate(conver);
            if (!violations.isEmpty()) {
                String erros = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(Map.of("erro", erros));
            }
//            URI uri = builder.path("/portaria/v1/solicitacao/visualizar/registro?registroId=5").build().toUri();
            return ResponseEntity.ok().body(Map.of("msg", resposta));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/lista/historico")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'VISUALIZAR_REGISTRO','GERENCIAR_REGISTROS')")
    public ResponseEntity<Page<RequestPortariaDTO>> lista(Pageable pageable,
                                                          @RequestParam("filial") Integer filial,
                                                          @RequestParam("busca") String busca) throws IOException {
        try {
            return ResponseEntity.ok().body(service.lista(pageable,filial,busca));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/lista/entradas/pendentes")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'VISUALIZAR_REGISTRO')")
    public ResponseEntity<Page<RequestPortariaDTO>> listaEntrada(Pageable pageable,
                                                                 @RequestParam("filial") Integer filial,
                                                                 @RequestParam(value = "busca",required = false) String busca,
                                                                 @RequestParam(value = "status",required = false) String status
    ) throws IOException {
        try {
            return ResponseEntity.ok().body(service.listaPendentes(pageable,filial,busca,status));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    // migrar para methods facture futuramente

    @PutMapping("/aguardando_Entrada")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRAR_ENTRADA','REGISTRAR_SAIDA')")
    @Transactional
    public ResponseEntity<StatusAtualizadoDTO> atualizaEntrada(@RequestParam("data") String data,
                                                               @RequestParam("file") MultipartFile file) {
        AtualizaStatus resposta = converteJson.conversor(data,AtualizaStatus.class);
       var atualizado = service.atualizaEntrada(resposta.usuarioId(),resposta.registroId(),file);
        return ResponseEntity.ok().body(atualizado);
    }
    // atualiza status aguardando saida para saiu
    @PutMapping("/aguardando_saida")
    @Transactional
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRAR_SAIDA','REGISTRAR_ENTRADA')")
    public ResponseEntity<StatusAtualizadoDTO> atualizaSaida(@RequestParam("data") String data,
                                                             @RequestParam("file") MultipartFile file) {
        AtualizaStatus resposta = converteJson.conversor(data,AtualizaStatus.class);
        var atualizado = service.atualizaSaida(resposta.usuarioId(),resposta.registroId(),file);
        return ResponseEntity.ok().body(atualizado);
    }
    // busca todas as soclitacoes com status de aguardando saida e entrada
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_REGISTROS')")
    @GetMapping("solicitacao/status")
    public ResponseEntity<Map<String,List<RequestPortariaDTO>>> listaSolicitacaoUsuario(@RequestParam("status") String status) {
        var resposta = service.FindaAllStatus(status);
        return ResponseEntity.ok().body(resposta);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'VISUALIZAR_REGISTRO')")
    @GetMapping("solicitacao/autorizador")
    public ResponseEntity<Page<RequestPortariaDTO>> meusRegistrosVisitantes(@RequestParam("usuario_id") Long usuarioID,
                                                                            Pageable page,
                                                                            @RequestParam(value = "busca",required = false) String busca) {
        Page<RequestPortariaDTO> pageresponse= service.solicitacaoUsuario(usuarioID,page,busca);
        return ResponseEntity.ok().body(pageresponse);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_REGISTROS')")
    @GetMapping("findAll")
    public ResponseEntity<Page<RequestPortariaDTO>> findAllRegistros(@RequestParam(value = "filial",required = false) Integer filial,
                                                                     Pageable page,
                                                                     @RequestParam(value = "busca",required = false) String busca,
                                                                     @RequestParam(value = "ativo",required = false) String ativo) {
        Boolean ativoBool = null;
        if (ativo != null) {
            ativoBool = Boolean.parseBoolean(ativo);
        }
        Page<RequestPortariaDTO> pageresponse= service.FindAllPortarias(page,filial,busca,ativoBool);
        return ResponseEntity.ok().body(pageresponse);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'VISUALIZAR_REGISTRO')")
    @GetMapping("solicitacao/visualizar/registro")
    public  ResponseEntity<RequestPortariaDTO> visualizarRegistro(@RequestParam("registroId") Long registro) {
        RequestPortariaDTO portariaDTO = service.visualizarRegistro(registro);
        return ResponseEntity.ok().body(portariaDTO);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'DELETAR_REGISTRO','DELETE_GLOBAL')")
    @DeleteMapping("solicitacao/delete/registro")
    public ResponseEntity<?> deleteRegistroPortaria(@RequestParam("registroId") Long registro,
                                                    @RequestParam("usuarioId") Long usuarioId){
       try {
           service.deleteRegistroPortaria(registro,usuarioId);
        return ResponseEntity.ok(Map.of("msg","Deletado com Sucesso"));
       }catch (RuntimeException e){
           throw new RuntimeException(e.getMessage());
       }
    }
    @PutMapping("/update")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'CRIAR_REGISTRO')")
    public ResponseEntity<Map<String,String>> atualizaRegistro(@RequestBody @Valid AtualizaRegistro update){
          try {
              service.atualizaRegistro(update);
              return ResponseEntity.ok().body(Map.of("msg","Atualizado com sucesso"));
          }catch (Exception e){
              throw new RuntimeException(e.getMessage());
          }
    }
    @PostMapping("/factory")
    public ResponseEntity<?> registroPortariaRequest(@RequestPart("data") @Valid String jsonString, @RequestParam(value = "file",required = false) MultipartFile file) throws JsonProcessingException {
        CadastroTypeFacture mapper =
                new ObjectMapper().readValue(jsonString, CadastroTypeFacture.class);
        Set<ConstraintViolation<CadastroTypeFacture>> violations =
                validator.validate(mapper);

        if (!violations.isEmpty()) {
            Map<String, String> erros = new HashMap<>();
            for (ConstraintViolation<?> v : violations) {
                erros.put(v.getPropertyPath().toString(), v.getMessage());
            }

            return ResponseEntity.badRequest().body(Map.of("erro", erros));

        }

       String smg = service.registroPortariaRequest(mapper,file);
        return ResponseEntity.ok().body(Map.of("msg",smg));
    }
}
