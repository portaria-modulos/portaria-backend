package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.portariacd.modulos.Moduloportaria.controllers.BlocoControler.BiometriaImagem;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioProjection;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.ImportacaoUsuarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.FactoryResponseChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.chaves.UsuarioConsumerUpdateDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.config.ConverteJson;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioConsumerService;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioConsumerImportService;
import com.portariacd.modulos.Moduloportaria.services.blocoChavesService.UsuarioId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("consumer")
public class ControlerConsumerUsuario {
    @Autowired
    private ConverteJson converteJson;
    @Autowired
    private Validator validator;
    @Autowired
    private UsuarioConsumerService service;
    @Autowired
    private UsuarioConsumerImportService importService;
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRO_CRIADO')")
    @PostMapping
    public ResponseEntity<Map<String,String>> cadastroUsuario(@RequestPart("res") @Valid String res, @RequestParam(value = "file",required = true)MultipartFile file) throws Exception {
        var conv = converteJson.conversorChaves(res, FactoryResponseChaves.class);
        Set<ConstraintViolation<Object>> violations = validator.validate(conv);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException("Erro de validação: " + sb.toString());
        }

        // 3. Segue o fluxo normal
        var resposta = service.cadastroDeUsuario(conv, file);
        return ResponseEntity.ok(resposta);
    }

    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRO_CRIADO')")
    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    public ResponseEntity<ImportacaoUsuarioResponseDTO> importarUsuarios(
            @RequestParam("planilha") MultipartFile planilha) throws Exception {
        return ResponseEntity.ok(importService.importar(planilha));
    }

    @PreAuthorize("@permissaoService.hasPermission(authentication, 'REGISTRO_CRIADO')")
    @PutMapping
    public ResponseEntity<Map<String, String>> atualizaUsuario(
            @RequestParam("usuarioId") Long usuarioId,
            @RequestPart("res") String res,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        var update = converteJson.conversor(res, UsuarioConsumerUpdateDTO.class);
        var resposta = service.atualizaUsuario(usuarioId, update, file);
        return ResponseEntity.ok(resposta);
    }
    @GetMapping
    public ResponseEntity<List<UsuarioConsumerResponseDTO>> lista(
            @RequestParam("filial") Integer filial){
        var resposta = service.lista(filial);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/biometria/user")
    public ResponseEntity<UsuarioProjection> Validbiometria(@RequestParam("file") MultipartFile file){
        UsuarioProjection l = service.extrairEmbedding(file);
        return ResponseEntity.ok(l);
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<DevolucaoInteface> deleteUsuario(@RequestParam(name = "usuarioId") Long id){
        DevolucaoInteface l = service.deleteUsuario(id);
        return ResponseEntity.ok(l);
    }
}
