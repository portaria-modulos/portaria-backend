package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioConsumerRepository extends JpaRepository<UsuarioConsumerChaves,Long> {
    @Query("""
    select u
    from UsuarioConsumerChaves u
    where u.matricula = :valor
       or u.nome = :valor
       or u.GmcoreId = :valor""")
    Optional<UsuarioConsumerChaves> buscaUsuario(@Param("valor") String valor);
    @Query("""
    select u
    from UsuarioConsumerChaves u
    where u.matricula = :matricula
       or u.GmcoreId = :s""")
    Optional<UsuarioConsumerChaves> findByUsuario(String s, String matricula);
}
