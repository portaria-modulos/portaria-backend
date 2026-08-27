package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAuditoriaChavesRepository extends JpaRepository<LogAuditoriaChavesEntity, Long>,
        JpaSpecificationExecutor<LogAuditoriaChavesEntity> {
}
