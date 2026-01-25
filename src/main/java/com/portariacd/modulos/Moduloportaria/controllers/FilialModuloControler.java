package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoFilialUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filial.JsonModuleFilial;
import com.portariacd.modulos.Moduloportaria.services.UsuarioModuloFilialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/module/v1/filial")
public class FilialModuloControler {
    private final UsuarioModuloFilialService service;
    public FilialModuloControler(UsuarioModuloFilialService service){
        this.service = service;
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    @PutMapping("/add/{usuarioId}")
    public ResponseEntity<?> addAcess(@RequestBody @Valid JsonModuleFilial module, @PathVariable Long usuarioId){
        service.addPermission(module.lista(),usuarioId);
        return ResponseEntity.ok().body(Map.of("msg","Adicionando com sucesso!"));
    };
    @GetMapping("/lista")
    public ResponseEntity<List<SistemaAcessoFilialUsuarioDTO>> lista(){
        var lista = service.lista();
        return  ResponseEntity.ok().body(lista);
    }
}
