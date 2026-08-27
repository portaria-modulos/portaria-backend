package com.portariacd.modulos.Moduloportaria.controllers.controleChaves;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves.auditoria.LogAuditoriaChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/logs-auditoria-chaves")
public class LogsAuditoriaChavesController {
    private final AuditoriaChavesService service;

    public LogsAuditoriaChavesController(AuditoriaChavesService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'VISUALIZAR_LOGS_CHAVES')")
    public ResponseEntity<Page<LogAuditoriaChavesResponseDTO>> listar(
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFinal,
            @RequestParam(required = false) AcaoAuditoriaChaves acao,
            @RequestParam(required = false) ModuloAuditoriaChaves modulo,
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) Long entidadeId,
            @RequestParam(required = false) Long usuarioResponsavelId,
            @RequestParam(required = false) Long filial,
            @RequestParam(required = false) Long blocoId,
            @RequestParam(required = false) Long armarioId,
            @RequestParam(required = false) Long chaveId) {
        return ResponseEntity.ok(service.listar(pageable, dataInicial, dataFinal, acao, modulo, entidade,
                entidadeId, usuarioResponsavelId, filial, blocoId, armarioId, chaveId));
    }
}
