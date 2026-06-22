package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.services.HistoryService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.historyDTO.HistoryDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("historico")
public class HistoryControler {
    private final HistoryService service;
    public HistoryControler(HistoryService service){
        this.service = service;
    }
   @GetMapping("/lista")
    public ResponseEntity<Page<HistoryDTO>> historico(Pageable page, @RequestParam(value = "busca",required = false) String busca){
        var lista = service.historico(page,busca);
        return ResponseEntity.ok(lista);
    }
    @PutMapping("/delete")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'DELETE_GLOBAL')")
    @Transactional
    public ResponseEntity<String> deletar(@RequestParam("id") Long id,@RequestParam("usuario_id") Long usuarioId){
        service.delete(id,usuarioId);
        return ResponseEntity.noContent().build();
    }
}
