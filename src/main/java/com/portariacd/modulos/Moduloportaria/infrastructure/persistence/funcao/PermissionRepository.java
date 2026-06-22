package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity,Long> {
    boolean existsByName(String nome);
}
