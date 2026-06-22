package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaAcessoUsuarioDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil.JsonModule;
import com.portariacd.modulos.Moduloportaria.services.UsuarioModuloService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/module")
public class ModuloControler {
    private final UsuarioModuloService service;
    public ModuloControler(UsuarioModuloService service){
        this.service = service;
    }
   @PostMapping("/add/{usuarioId}")
    public ResponseEntity<?> addAcess(@RequestBody @Valid JsonModule module, @PathVariable Long usuarioId){
        service.addPermission(module.lista(),module.mod(),usuarioId);
        return ResponseEntity.ok().body(Map.of("msg","Adicionando com sucesso!"));
    }
    @GetMapping("/lista")
    public ResponseEntity<List<SistemaAcessoUsuarioDTO>> lista(){
        var lista = service.lista();
        return  ResponseEntity.ok().body(lista);
    }
}
