package com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioProjection;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    @Query(value = """
            SELECT
                u.id,
                u.nome,
                b.embedding <=> CAST(:vetor AS vector) AS distancia
             FROM USUARIO_CONSUMER_CHAVES u
        LEFT JOIN BIOMETRIA_FACIAL b u ON u.biometria_facial_id= b.id
            ORDER BY b.embedding <=> CAST(:vetor AS vector)
            LIMIT 5;
    """, nativeQuery = true)
    UsuarioProjection buscarUsuarioPelaBiometriad(
            @Param("vetor") String vetor
    );
    @Query(value = """
        SELECT
            u.id,
            u.matricula,
            u.gmcore_id AS gmcoreId,
            u.nome,
            u.setor,
            u.filial,
            u.ativo
        FROM USUARIO_CONSUMER_CHAVES u
        LEFT JOIN BIOMETRIA_FACIAL b
            ON u.biometria_facial_id= b.id
        WHERE (b.embedding <=> (:vetor)::vector) < 0.35
        ORDER BY b.embedding <=> (:vetor)::vector
        LIMIT 5;
""", nativeQuery = true)
    UsuarioProjection buscarUsuarioPelaBiometria(
            @Param("vetor") String vetor
    );
    @Query("Select p from UsuarioConsumerChaves p")
    List<UsuarioConsumerChaves> findAllUsuario();
}
