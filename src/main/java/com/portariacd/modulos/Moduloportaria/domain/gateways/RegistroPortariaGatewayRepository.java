package com.portariacd.modulos.Moduloportaria.domain.gateways;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.AtualizaRegistro;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.EmTeste.RegistroPortariaRequestDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.RequestPortariaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.RegistroPortaria.StatusAtualizadoDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.facture.CadastroTypeFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RegistroPortariaGatewayRepository {
     String registroPortaria(RegistroPortariaRequestDTO request, MultipartFile file);

     Page<RequestPortariaDTO> listaPostaria(Pageable pageable, Integer filial,String busca);

     StatusAtualizadoDTO atualizaEntrada(Long ficalId, long registroId, MultipartFile file);
     StatusAtualizadoDTO atualizaSaida(Long ficalId,long registroId,MultipartFile file);

    Page<RequestPortariaDTO> solitacaoUsuario(Long usuarioID,Pageable page,String busca);
     Map<String, List<RequestPortariaDTO>> FindAllStatus(String Status);

     Page<RequestPortariaDTO> listaPendentes(Pageable pageable, Integer filial,String busca,String status);

    RequestPortariaDTO visulizarRegistro(Long registro);
    void deleteRegistroPortaria(Long registroId,Long usuarioId);

    void atualizaRegistro(AtualizaRegistro update);
    Page<RequestPortariaDTO> FindAllPortarias(Pageable pageable, Integer filial, String busca, Boolean ativo,
                                              LocalDate data,String status);
    String registroPortariaRequest(CadastroTypeFacture data, MultipartFile file);


}
