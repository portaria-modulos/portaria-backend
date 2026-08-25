package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.ArmarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.armario.ArmarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.DesvolucaoChaveDto;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.EntregaChavesCDService;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.EntregaChavesDTO;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method.DesvolucaoChaveUsuarioDto;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.method.FactureMetodChave;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("entregaChaves")
public class EntregaDeChaves {
    @Autowired
    private EntregaChavesCDService service;
    @PostMapping
    public ResponseEntity<DevolucaoInteface> entregaChaves(@RequestBody @Valid EntregaChavesDTO a){
       var response =  service.entregaDeChaves(a);
        return ResponseEntity.ok(response);
    }

    @PostMapping("devolucao")
    public ResponseEntity<DevolucaoInteface> devolucaoChaves(@RequestBody @Valid DesvolucaoChaveDto a){
        return ResponseEntity.ok(service.liberacaoDeChaves(a));
    }
    @PostMapping("devolucao/token")
    public ResponseEntity<DevolucaoInteface> devolucaoChavesToekn(@RequestBody @Valid EntregaChavesDTO dto){
        return ResponseEntity.ok(service.devolverChavePorToken(dto));
    }
    @GetMapping("lista")
    public ResponseEntity<List<ArmarioResponseDTO>> ListaDeArmariosFilial(@RequestParam("filial") Integer filial){
        return ResponseEntity.ok(service.listaDeArmariosCd(filial));
    }
    @GetMapping("top3/{filial}/arm/{id}")
    public ResponseEntity<?> Ultims(@PathVariable Long filial,@PathVariable Long id){
        var lista = service.TresUlmicasRetiradas(filial,id);
        return ResponseEntity.ok(lista);
    }
    @GetMapping("detalhes/arm/{arm}/chave/{chave}/filial/{filial}")
    public ResponseEntity<?> Detalhes(
            @PathVariable Long arm,
            @PathVariable Integer chave,
            @PathVariable Long filial) {

        var lista = service.detalhesChaves(filial, arm, chave);
        return ResponseEntity.ok(lista);
    }
    @PostMapping("devolucao/user")
    public ResponseEntity<String> devolucaoChavesUser(@RequestBody @Valid FactureMetodChave a){
        return ResponseEntity.ok(service.liberacaoDeChavesFacture(a));
    }

    @GetMapping("ocupados/arm/filial")
    public ResponseEntity<?> ocupadaFilial(
            @RequestParam("arm") Long arm,
            @RequestParam("filial") Long filial) {

        var lista = service.ocupadoFilial(filial, arm);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("relatorio")
    public ResponseEntity<?> relatorio(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) Long filial,
            @RequestParam(required = false) String colaborador,
            @RequestParam(required = false) String modeloArmario,
            @RequestParam(required = false, defaultValue = "TODOS") String status,
            @RequestParam(required = false, defaultValue = "MES") String agrupamento) {
        return ResponseEntity.ok(service.relatorio(dataInicio, dataFim, filial, colaborador,
                modeloArmario, status, agrupamento));
    }



}
