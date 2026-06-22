package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.services.VisitanteService;
import com.portariacd.modulos.Moduloportaria.domain.models.visitante.StatusTypeDeleteVisitante;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.VisitanteDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.VisitanteFiltroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("visitante")
public class VisitanteControler {
    private final VisitanteService service;
    public VisitanteControler(VisitanteService service){
        this.service = service;

    }
    @GetMapping("/lista")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    public ResponseEntity<Page<VisitanteDTO>> listVisitante(
            Pageable page,
            @RequestParam(name = "busca",required = false) String busca,
            @RequestParam(name = "filial",required = false) Integer filial
    ){
        var lista = service.listaVisitante(page,busca,filial);
        return ResponseEntity.ok(lista);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'DELETE_GLOBAL')")
    @DeleteMapping("/delete/registro")
    public ResponseEntity<?> deleteRegistroPortaria(@RequestParam("visitanteId") Long visitanteId,
                                                    @RequestParam("usuarioId") Long usuarioId,
                                                    @RequestParam("type") String type
    ){
        try {
          String msg = service.deletarVisitenate(visitanteId,usuarioId,StatusTypeDeleteVisitante.StatusAdd(type.toLowerCase()));
            return ResponseEntity.ok(Map.of("msg",msg));
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/placa")
    public VisitanteDTO buscaVisitante(@RequestParam(name = "visitanteId",required = false) Long visitanteId,@RequestParam(name = "nome",required = false) String nome) {
        return service.buscaVisitante(visitanteId,nome);
    }
    @GetMapping("/filtro")
    public ResponseEntity<Page<VisitanteFiltroDTO>> FiltroFisitanteLista(Pageable page){
        var lista = service.listaVisitanteFiltro(page);
        return ResponseEntity.ok(lista);
    }
}
