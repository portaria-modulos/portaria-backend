package com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves.auditoria;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesEntity;

import java.time.OffsetDateTime;
import java.util.Map;

public record LogAuditoriaChavesResponseDTO(
        Long id,
        OffsetDateTime dataHora,
        AcaoAuditoriaChaves acao,
        ModuloAuditoriaChaves modulo,
        String entidade,
        Long entidadeId,
        String descricao,
        Long usuarioResponsavelId,
        String usuarioResponsavelNome,
        Long filial,
        Long blocoId,
        Long armarioId,
        Long chaveId,
        Map<String, Object> valorAnterior,
        Map<String, Object> valorNovo,
        Integer quantidadeAnterior,
        Integer quantidadeNova,
        String enderecoIp,
        String userAgent
) {
    public LogAuditoriaChavesResponseDTO(LogAuditoriaChavesEntity log) {
        this(log.getId(), log.getDataHora(), log.getAcao(), log.getModulo(), log.getEntidade(),
                log.getEntidadeId(), log.getDescricao(), log.getUsuarioResponsavelId(),
                log.getUsuarioResponsavelNome(), log.getFilial(), log.getBlocoId(),
                log.getArmarioId(), log.getChaveId(), log.getValorAnterior(), log.getValorNovo(),
                log.getQuantidadeAnterior(), log.getQuantidadeNova(), log.getEnderecoIp(),
                log.getUserAgent());
    }
}
