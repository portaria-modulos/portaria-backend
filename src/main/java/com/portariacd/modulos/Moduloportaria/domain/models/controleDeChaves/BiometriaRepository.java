package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BiometriaRepository extends JpaRepository<BiometriaFacial,Long> {

    @Query(value = """

            SELECT
                                                           u.id,
                                                           u.matricula,
                                                           u.gmcore_id AS gmcoreId,
                                                           u.nome,
                                                           u.setor,
                                                           u.filial,
                                                           u.ativo
                                                       FROM BIOMETRIA_FACIAL b
                                                       JOIN USUARIO_CONSUMER_CHAVES u
                                                           ON u.id = b.usuario_id
                                                       WHERE b.embedding <=> CAST(:vetor AS vector) < 0.35
                                                       ORDER BY b.embedding <=> CAST(:vetor AS vector)
                                                       LIMIT 1;
    """, nativeQuery = true)
    UsuarioProjection buscarUsuarioPelaBiometria(
            @Param("vetor") String vetor
    );
}
