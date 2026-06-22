package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<PerfilEntity,Long> {
    boolean existsByNome(String nome);
}
