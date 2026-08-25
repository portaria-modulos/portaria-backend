package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.ArmarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ArmarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.ArmarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.BlocoChavesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BlocoChaveServices {
    @Autowired
   private ArmarioRepository repository;
    @Autowired
    private BlocoChavesRepository chavesRepository;

   public void criaArmario(ArmarioDTO dto){
    Optional<Armario> response = repository.findByFilial(dto.filial(), Tipo.convertTipo(dto.tipo()));
    if(response.isPresent()){
        throw new RuntimeException("Ja contém armario criado");
    }
      repository.save(new Armario(dto));
   }

   public List<ArmarioResponseDTO> listarArmariosComProblema(Integer filial, String tipo) {
       Tipo tipoFiltro = tipo == null || tipo.isBlank() ? null : Tipo.convertTipo(tipo);
       return repository.findAll().stream()
               .filter(armario -> filial == null || filial.longValue() == armario.getFilial())
               .filter(armario -> tipoFiltro == null || armario.getTipo() == tipoFiltro)
               .filter(armario -> armario.getBlocoChaves().stream()
                       .anyMatch(chave -> statusImpedeUso(chave.getStatus()) || !chave.isAtivo()))
               .map(ArmarioResponseDTO::new)
               .toList();
   }

   public BlocoChaves alterarStatusBloco(Long armarioId, Long blocoId, Integer numeroChave,
                                         String tipo, String status) {
       var armario = repository.findById(armarioId)
               .orElseThrow(() -> new RuntimeException("Armário não encontrado"));
       validarTipo(armario, tipo);
       var bloco = armario.getBlocoChaves().stream()
               .filter(chave -> blocoId != null
                       ? chave.getId().equals(blocoId)
                       : chave.getNumero().equals(numeroChave))
               .findFirst()
               .orElseThrow(() -> new RuntimeException("Chave não encontrada no armário"));
       StatusArmario novoStatus = converterStatus(status);
       bloco.setStatus(novoStatus);
       boolean ativo = novoStatus == StatusArmario.LIVRE || novoStatus == StatusArmario.OCUPADO;
       bloco.setAtivo(ativo);
       bloco.setDisponivel(novoStatus == StatusArmario.LIVRE);
       return chavesRepository.save(bloco);
   }

   private void validarTipo(Armario armario, String tipo) {
       if (tipo != null && !tipo.isBlank()
               && armario.getTipo() != Tipo.convertTipo(tipo)) {
           throw new RuntimeException("O tipo informado não pertence ao armário");
       }
   }

   private boolean statusImpedeUso(StatusArmario status) {
       return status == StatusArmario.EM_MANUTENCAO
               || status == StatusArmario.BLOQUEADO
               || status == StatusArmario.INATIVO
               || status == StatusArmario.SEM_ACESSO
               || status == StatusArmario.SEM_CHAVE;
   }

   private StatusArmario converterStatus(String status) {
       try {
           return StatusArmario.valueOf(status.trim().toUpperCase());
       } catch (IllegalArgumentException e) {
           return StatusArmario.converteTipoArmario(status.trim());
       }
   }
    public List<ArmarioResponseDTO> visualizarArmarios() {
        return repository.findAll()
                .stream()
                .map(armario -> new ArmarioResponseDTO(
                        armario.getId(),
                        armario.getFilial(),
                        armario.getTipo().name(),
                        armario.getBlocoChaves()
                                .stream()
                                .sorted(Comparator.comparing(BlocoChaves::getNumero))
                                .map(BlocoChavesResponseDTO::new)
                                .toList()
                ))
                .toList();
    }
    
    public List<BlocoChavesResponseDTO> cadastrarChaves(BlocoChavesDTO bloco) {
       var armario = repository.findById(bloco.amarioId()).orElseThrow(
               ()-> new RuntimeException("Armarios não encontrado")
       );
        Integer ultimoNumero = chavesRepository.buscarUltimoNumero(bloco.amarioId());
        List<BlocoChaves> novosBlocos = new ArrayList<>();
        for (var i=1;i<=bloco.quantidade();i++){
            BlocoChaves chave = new BlocoChaves();
            chave.setNumero(ultimoNumero + i);
            chave.setArmario(armario);
            chave.setDisponivel(true);
            chave.setAtivo(true);
            chave.setStatus(StatusArmario.LIVRE);
            novosBlocos.add(chave);
        }
        if(novosBlocos.size() > 1000){
            throw new RuntimeException("Limiete maximo de armarios 1000");
        }
        if(ultimoNumero.longValue()<100) {
            var salvos = chavesRepository.saveAll(novosBlocos);
            return salvos.stream()
                    .map(BlocoChavesResponseDTO::new)
                    .toList();
        }
        throw new RuntimeException("Limiete excedido");


    }
    @Cacheable(value = "armarios", key = "#filial + ':' + #tipo")
    public ArmarioResponseDTO unicoArmariodId(Long armarioId, Tipo tipo) {
        var armarios = repository.findAllArmario(armarioId,tipo);
        return new ArmarioResponseDTO(armarios);
    }
}
