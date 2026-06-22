package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.modulosPerfil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioModuloRepository extends JpaRepository<UsuarioModuloEntity,Long> {
   @Query("select p from UsuarioModuloEntity p where p.usuario.id=:usuarioId and modulo.id=:aLong")
    Optional<UsuarioModuloEntity> findByUsuarioIdAndModuloId(Long usuarioId, Long aLong);
}
