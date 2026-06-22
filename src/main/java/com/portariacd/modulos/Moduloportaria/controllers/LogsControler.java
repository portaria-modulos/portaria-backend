package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.services.LogAcaoService;
import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.LogAcaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LogsControler {
    private final LogAcaoService logAcaoService;
    public LogsControler(LogAcaoService logAcaoService){
        this.logAcaoService = logAcaoService;
    }
    @GetMapping("/lista")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_REGISTROS')")
    public ResponseEntity<Page<LogAcaoDTO>> listaAcoes(Pageable page, @RequestParam("busca") String buca){
       var busca = logAcaoService.listaAcoes(page,buca);
        return ResponseEntity.ok(busca);
    }
}
