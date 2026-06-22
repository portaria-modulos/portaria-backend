package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.SaidaVisitanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaPortaria extends JpaRepository<SaidaVisitanteEntity,Long> {
}
