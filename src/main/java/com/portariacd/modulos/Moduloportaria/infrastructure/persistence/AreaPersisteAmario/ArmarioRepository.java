package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ArmarioRepository extends JpaRepository<Armario,Long> {
    @Query(value = "SELECT a FROM Armario a where a.filial =:filial and a.tipo = :tipo ")
    Optional<Armario> findByFilial(Long filial, Tipo tipo);

    @Query(value = "SELECT a FROM Armario a where a.filial =:filial order by a.id ASC ")
    List<Armario> findAllFilial(Integer filial);

    @Query(value = "SELECT a FROM Armario a " +
            "WHERE a.id = :armarioId AND a.tipo = :tipo ")
    Armario findAllArmario(@Param("armarioId") Long armarioId, @Param("tipo") Tipo tipo);
}
