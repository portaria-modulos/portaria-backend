package com.portariacd.modulos.Moduloportaria.controllers.BlocoControler;

import com.portariacd.modulos.Moduloportaria.services.BlocoService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RequestBlocoDTO;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.BiometriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("bloco")
public class BlocoControler {
    private BiometriaService biometriaService;
    private final BlocoService service;
    public BlocoControler(BlocoService service,BiometriaService biometriaService){

        this.service = service;
        this.biometriaService = biometriaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RequestBlocoDTO>>> lista(){
        return ResponseEntity.ok().body(service.lista());
    }
    @PostMapping("/biometria")
    public float[] biometria(@RequestParam("file") @Valid MultipartFile file){
      return biometriaService.extrairEmbedding(file);
    }

}
