package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlocoChavesRepository extends JpaRepository<BlocoChaves,Long> {
    @Query("SELECT COALESCE(MAX(b.numero), 0) FROM BlocoChaves b where b.armario.id =:armarioId")
    Integer buscarUltimoNumero(Long armarioId);

     @Query("select p from BlocoChaves p left join p.armario ar where ar.filial = :filial and ar.id = :arm and p.numero = :chave ")
    BlocoChaves findChavaDetalhesFilial(Long filial, Long arm, Integer chave);
    @Query("select p from BlocoChaves p left join p.armario ar where ar.filial = :filial and ar.id = :arm and p.status = :status ")
    List<BlocoChaves> findChavaOcupadoFilial(Long filial, Long arm, StatusArmario status);
}
