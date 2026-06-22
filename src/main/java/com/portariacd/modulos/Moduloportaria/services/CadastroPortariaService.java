package com.portariacd.modulos.Moduloportaria.services;

import com.portariacd.modulos.Moduloportaria.domain.gateways.RegistroPortariaGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.AtualizaRegistro;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.RequestPortariaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.StatusAtualizadoDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CadastroPortariaService {
    private final RegistroPortariaGatewayRepository repository;
   public CadastroPortariaService(RegistroPortariaGatewayRepository repository){
        this.repository = repository;
    }

    public String cadastro(RegistroPortariaRequestDTO request, MultipartFile file){
       return repository.registroPortaria(request,file);

    }
    public Page<RequestPortariaDTO> lista(Pageable pageable, Integer filial,String busca){
     return repository.listaPostaria(pageable,filial,busca);
    }
    public StatusAtualizadoDTO atualizaEntrada(Long ficalId, long registroId, MultipartFile file) {
      return repository.atualizaEntrada(ficalId,registroId,file);
    }
    public StatusAtualizadoDTO atualizaSaida(Long ficalId, long registroId, MultipartFile file) {
        return repository.atualizaSaida(ficalId,registroId,file);
    }

    public Page<RequestPortariaDTO> solicitacaoUsuario(Long usuarioID,Pageable page,String busca) {
        return repository.solitacaoUsuario(usuarioID,page,busca);
    }
    public Map<String, List<RequestPortariaDTO>> FindaAllStatus(String status) {
        return repository.FindAllStatus(status);
    }
    public Page<RequestPortariaDTO> retornaEntradaIdVisitante(Pageable pageable,Long id,String busca,LocalDate dataAntes,LocalDate dataDepois) {
        return repository.retornaEntradaIdVisitante(pageable,id,busca,dataAntes,dataDepois);
    }

    public Page<RequestPortariaDTO> listaPendentes(Pageable pageable, Integer filial,String busca,String status) {
        return repository.listaPendentes(pageable,filial,busca,status);
    }
    public Page<RequestPortariaDTO> FindAllPortarias(Pageable pageable,
                                                     Integer filial,
                                                     String busca,
                                                     Boolean ativo, LocalDate data,String status) {
        return repository.FindAllPortarias(pageable,filial,busca,ativo,data,status);
    }

    public RequestPortariaDTO visualizarRegistro(Long registro) {
        return repository.visulizarRegistro(registro);
    }
    public void deleteRegistroPortaria(Long registroId,Long usuarioId){
       repository.deleteRegistroPortaria(registroId,usuarioId);
    }

    public void atualizaRegistro(AtualizaRegistro update) {
        repository.atualizaRegistro(update);
    }
    public String registroPortariaRequest(CadastroTypeFacture data, MultipartFile file) {
        return repository.registroPortariaRequest(data,file);
    }
}
