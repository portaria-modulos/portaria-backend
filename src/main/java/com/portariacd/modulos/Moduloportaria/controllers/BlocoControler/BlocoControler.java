package com.portariacd.modulos.Moduloportaria.controllers.BlocoControler;

import com.portariacd.modulos.Moduloportaria.services.BlocoService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RequestBlocoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("bloco")
public class BlocoControler {
    private final BlocoService service;
    public BlocoControler(BlocoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RequestBlocoDTO>>> lista(){
        return ResponseEntity.ok().body(service.lista());
    }
}
