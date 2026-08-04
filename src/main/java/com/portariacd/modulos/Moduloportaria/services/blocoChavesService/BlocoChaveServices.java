package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
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
