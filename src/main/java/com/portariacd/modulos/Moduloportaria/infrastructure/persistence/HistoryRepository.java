package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.history.HistoryEntradaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntradaEntity,Long> {
    HistoryEntradaEntity findByRegistroPortariaId(long registroId);
    @Query("""
            SELECT p FROM HistoryEntradaEntity p 
            WHERE
            LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.placaVeiculo) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.protocolo) LIKE LOWER(CONCAT('%', :busca, '%'))  
            """)
    Page<HistoryEntradaEntity> findbyBusca(Pageable page, String busca);

    List<HistoryEntradaEntity> findAllByVisitante(VisitanteEntity visitante);
}
