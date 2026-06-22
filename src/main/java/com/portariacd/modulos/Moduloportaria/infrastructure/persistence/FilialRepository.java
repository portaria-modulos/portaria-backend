package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilialRepository extends JpaRepository<Filial,Long> {
    @Query("select p from Filial p where p.numeroFilial=:numero or p.nome=:nome")
    Optional<Filial> findByNumeroFilial(Integer numero,String nome);
}
