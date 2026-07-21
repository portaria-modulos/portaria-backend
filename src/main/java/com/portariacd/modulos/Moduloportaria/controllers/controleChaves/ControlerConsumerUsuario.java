package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.portariacd.modulos.Moduloportaria.controllers.BlocoControler.BiometriaImagem;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioProjection;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.UsuarioConsumerRequestDTO;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioConsumerService;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioId;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("consumer")
public class ControlerConsumerUsuario {
    @Autowired
    private UsuarioConsumerService service;
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRO_CRIADO')")
    @PostMapping
    public ResponseEntity<Map<String,String>> cadastroUsuario(@RequestBody @Valid UsuarioConsumerRequestDTO res) throws Exception {
        var resposta =service.cadastroDeUsuario(res);
        return ResponseEntity.ok(resposta);
    }
    @GetMapping
    public ResponseEntity<List<UsuarioConsumerRequestDTO>> lista(){
        var resposta =service.lista();
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/biometria/user")
    public ResponseEntity<UsuarioProjection> Validbiometria(@RequestBody @Valid BiometriaImagem bio){
        UsuarioProjection l = service.extrairEmbedding(bio.base64());
        return ResponseEntity.ok(l);
    }
}
