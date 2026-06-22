package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EntregaChaveHistoryRepository extends JpaRepository<EntregaChaveHistory,Long> {
    @Query("""
    select e
    from EntregaChave e
    where e.blocoChaves.id = :chaveId
    and e.dataHoraDevolucao is null
""")
    Optional<EntregaChaveHistory> findEntregaAtiva(@Param("chaveId") Long chaveId);

    @Query("""
    select e
    from EntregaChave e
    where e.matriculaColaborador = :s
    and e.dataHoraDevolucao is null
""")
    Optional<EntregaChaveHistory> findEntregaUsuario( String s);
}
