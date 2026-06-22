package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.EntradaVisitanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaPortaria extends JpaRepository<EntradaVisitanteEntity,Long> {
}
