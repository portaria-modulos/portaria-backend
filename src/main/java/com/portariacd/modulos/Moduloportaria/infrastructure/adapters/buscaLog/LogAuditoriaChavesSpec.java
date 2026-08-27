package com.portariacd.modulos.Moduloportaria.infrastructure.adapters.buscaLog;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class LogAuditoriaChavesSpec {
    private LogAuditoriaChavesSpec() {
    }

    public static Specification<LogAuditoriaChavesEntity> dataInicial(OffsetDateTime dataInicial) {
        return (root, query, cb) -> dataInicial == null
                ? null
                : cb.greaterThanOrEqualTo(root.get("dataHora"), dataInicial);
    }

    public static Specification<LogAuditoriaChavesEntity> dataFinal(OffsetDateTime dataFinal) {
        return (root, query, cb) -> dataFinal == null
                ? null
                : cb.lessThanOrEqualTo(root.get("dataHora"), dataFinal);
    }

    public static Specification<LogAuditoriaChavesEntity> acao(AcaoAuditoriaChaves acao) {
        return (root, query, cb) -> acao == null ? null : cb.equal(root.get("acao"), acao);
    }

    public static Specification<LogAuditoriaChavesEntity> modulo(ModuloAuditoriaChaves modulo) {
        return (root, query, cb) -> modulo == null ? null : cb.equal(root.get("modulo"), modulo);
    }

    public static Specification<LogAuditoriaChavesEntity> entidade(String entidade) {
        return (root, query, cb) -> entidade == null || entidade.isBlank()
                ? null
                : cb.equal(cb.lower(root.get("entidade")), entidade.trim().toLowerCase());
    }

    public static Specification<LogAuditoriaChavesEntity> entidadeId(Long entidadeId) {
        return igualLong("entidadeId", entidadeId);
    }

    public static Specification<LogAuditoriaChavesEntity> usuarioResponsavelId(Long usuarioId) {
        return igualLong("usuarioResponsavelId", usuarioId);
    }

    public static Specification<LogAuditoriaChavesEntity> filial(Long filial) {
        return igualLong("filial", filial);
    }

    public static Specification<LogAuditoriaChavesEntity> blocoId(Long blocoId) {
        return igualLong("blocoId", blocoId);
    }

    public static Specification<LogAuditoriaChavesEntity> armarioId(Long armarioId) {
        return igualLong("armarioId", armarioId);
    }

    public static Specification<LogAuditoriaChavesEntity> chaveId(Long chaveId) {
        return igualLong("chaveId", chaveId);
    }

    private static Specification<LogAuditoriaChavesEntity> igualLong(String campo, Long valor) {
        return (root, query, cb) -> valor == null ? null : cb.equal(root.get(campo), valor);
    }
}
