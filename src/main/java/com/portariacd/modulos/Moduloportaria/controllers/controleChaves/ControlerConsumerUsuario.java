package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioConsumerService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("consumer")
public class ControlerConsumerUsuario {
    @Autowired
    private UsuarioConsumerService service;

    @PostMapping
    public ResponseEntity<Map<String,String>> cadastroUsuario(@RequestBody @Valid UsuarioConsumerRequestDTO res){
        var resposta =service.cadastroDeUsuario(res);
        return ResponseEntity.ok(resposta);
    }
    @GetMapping
    public ResponseEntity<List<UsuarioConsumerRequestDTO>> lista(){
        var resposta =service.lista();
        return ResponseEntity.ok(resposta);
    }
}
