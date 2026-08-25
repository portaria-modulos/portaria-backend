package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.ArmarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ArmarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves.AlterarStatusArmarioDTO;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.BlocoChaveServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("armario")
public class ArmarioBlocosControler {
    @Autowired
    private BlocoChaveServices service;
   @PostMapping
    public ResponseEntity<Map<String,String>> cadastroArmarios(@RequestBody @Valid ArmarioDTO dto){
        service.criaArmario(dto);
        return ResponseEntity.ok(Map.of("msg","Criado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<ArmarioResponseDTO>> visualizarArmario(){
       var s = service.visualizarArmarios();
       return ResponseEntity.ok(s);
    }

    @GetMapping("/problemas")
    public ResponseEntity<List<ArmarioResponseDTO>> listarArmariosComProblema(
            @RequestParam("filial") Integer filial,
            @RequestParam(value = "tipo", required = false) String tipo){
        return ResponseEntity.ok(service.listarArmariosComProblema(filial, tipo));
    }

    @PatchMapping("/{armarioId}/status")
    @PutMapping("/{armarioId}/status")
    public ResponseEntity<Map<String, String>> alterarStatus(
            @PathVariable Long armarioId,
            @RequestBody @Valid AlterarStatusArmarioDTO dto) {
        var status = service.alterarStatusArmario(armarioId, dto.status());
        return ResponseEntity.ok(Map.of("armarioId", String.valueOf(armarioId),
                "statusBlocos", status.name()));
    }

    @PutMapping("/{armarioId}/chave/{numeroChave}/status")
    public ResponseEntity<BlocoChavesResponseDTO> alterarStatusBloco(
            @PathVariable Long armarioId,
            @PathVariable Integer numeroChave,
            @RequestBody @Valid AlterarStatusArmarioDTO dto) {
        var bloco = service.alterarStatusBloco(armarioId, numeroChave, dto.status());
        return ResponseEntity.ok(new BlocoChavesResponseDTO(bloco));
    }
    @PostMapping("/chaves")
    public ResponseEntity<List<BlocoChavesResponseDTO>> cadastreEspacoChaves(@RequestBody @Valid BlocoChavesDTO s){
        var d = service.cadastrarChaves(s);
        return ResponseEntity.ok(d);
    }

    @GetMapping("unico")
    public ResponseEntity<ArmarioResponseDTO> visualizarArmario(@RequestParam("armarioId") Long armariodID,@RequestParam("tipo") Tipo blocoId){
        var s = service.unicoArmariodId(armariodID,blocoId);
        return ResponseEntity.ok(s);
    }

}
