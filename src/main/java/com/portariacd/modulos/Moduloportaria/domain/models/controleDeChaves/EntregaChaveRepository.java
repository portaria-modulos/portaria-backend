package com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EntregaChaveRepository extends JpaRepository<EntregaChave,Long> {
    @Query("""
    select e
    from EntregaChave e
    where e.blocoChaves.id = :chaveId
    and e.dataHoraDevolucao is null and e.entregue = false
""")
    Optional<EntregaChave> findEntregaAtiva(@Param("chaveId") Long chaveId);

    @Query("""
    select e
    from EntregaChave e
    where e.matriculaColaborador = :s
    and e.blocoChaves.armario.id = :id
    and e.dataHoraDevolucao is null and e.entregue = false
""")
    Optional<EntregaChave> findEntregaUsuario(String s, long id);
    @Query(value = """
WITH ultimas_entregas AS (
    SELECT
        e.*,
        ROW_NUMBER() OVER (
            PARTITION BY b.armario_id
            ORDER BY e.data_hora_retirada DESC
        ) AS rn
    FROM entrega_chave e
    INNER JOIN BLOCO_ARMARIO b
        ON b.id = e.bloco_chaves_id
    INNER JOIN ARMARIO a
        ON a.id = b.armario_id
    WHERE a.filial = :filial
    and a.id = :id
)
SELECT *
FROM ultimas_entregas
WHERE rn <= 1
""", nativeQuery = true)
    List<EntregaChave> findTop3PorArmarioDaFilial(@Param("filial") Long filial,@Param("id") Long id);

    List<EntregaChave> id(long id);
    @Query("select chave from EntregaChave chave where chave.usuarioIdRetirada = :idUsuario and chave.entregue = false")
    List<EntregaChave> entregaChaveUsuarioAtivoFalse(@Param("idUsuario") Long idUsuario);
}
