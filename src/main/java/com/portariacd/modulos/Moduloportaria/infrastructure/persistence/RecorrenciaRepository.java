package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecorrenciaRepository extends JpaRepository<Recorrencia,Long> {
    Optional<Recorrencia> findByNome(String nome);
}
