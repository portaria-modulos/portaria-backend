package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos.BlocoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlocoRepository extends JpaRepository<BlocoEntity,Long> {
    Optional<BlocoEntity> findByNome(String nome);
}
