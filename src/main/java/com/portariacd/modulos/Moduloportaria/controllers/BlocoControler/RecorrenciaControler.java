package com.portariacd.modulos.Moduloportaria.controllers.BlocoControler;

import com.auth0.jwt.interfaces.Claim;
import com.portariacd.modulos.Moduloportaria.services.RecorrenciaService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RequestRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.config.TokenConfigure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("recorrencia")
public class RecorrenciaControler {
    private final RecorrenciaService service;
    private final TokenConfigure configure;
    public RecorrenciaControler(RecorrenciaService service,
                                TokenConfigure configure
    ){
        this.service = service;
        this.configure = configure;
    }


    @GetMapping
    public ResponseEntity<Map<String, List<RequestRecorrenciaDTO>>> lista(@RequestHeader("Authorization") String token){
        String jwt = token.replace("Bearer ", "");
        Map<String, Claim> claimMap = configure.validaTokenAuth(jwt);
        var UsuarioId = claimMap.get("id").asLong();
        return ResponseEntity.ok().body(service.lista(UsuarioId));
    }
}
