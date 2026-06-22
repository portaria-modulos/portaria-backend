package com.portariacd.modulos.Moduloportaria.controllers.filial;

import com.portariacd.modulos.Moduloportaria.services.FilialService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RegistroFilialDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.filialDTO.RequestFilialDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.config.TokenConfigure;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("filial")
public class FilialControler {
    private final FilialService service;
    public FilialControler(FilialService service,
                           TokenConfigure configure
    ){
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<Map<String, List<RequestFilialDTO>>> lista(){
        return ResponseEntity.ok().body(service.lista());
    }
    @PostMapping("/registro")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    public ResponseEntity<Map<String, String>> lista(@RequestBody @Valid RegistroFilialDTO registroFilialDTO){
        service.registro(registroFilialDTO);
        return ResponseEntity.ok().body(Map.of("msg","Registrado com sucesso!"));
    }
}
