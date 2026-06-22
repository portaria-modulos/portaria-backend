package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.filialPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioFiliaisRepository extends JpaRepository<UsuarioFilialEntity,Long> {
   @Query("select p from UsuarioFilialEntity p where p.usuario.id=:usuarioId and filial.id=:aLong")
    Optional<UsuarioFilialEntity> findByUsuarioIdAndFilialId(Long usuarioId, Long aLong);
    @Query("select p from UsuarioFilialEntity p where p.usuario.id = :id")
    List<UsuarioFilialEntity> findAllUsuario(Long id);
}
