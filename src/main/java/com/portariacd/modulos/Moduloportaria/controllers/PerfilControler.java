package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.services.PerfilService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.perfilDTO.PerfilResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("perfil")
public class PerfilControler {
    private final PerfilService service;
    public PerfilControler(PerfilService service){
        this.service = service;
    }
    @GetMapping("/lista")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    public ResponseEntity<Map<String, List<PerfilResponseDTO>>> listaPerfils() {
        var response = service.listaPerfils();
        return ResponseEntity.ok(response);
    }
}
