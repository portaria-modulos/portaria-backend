package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.domain.models.registro_visitante.StatusPortaria;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroVisitanteRepository extends JpaRepository<RegistroVisitantePortariaEntity,Long>,
        JpaSpecificationExecutor<RegistroVisitantePortariaEntity>
{

    @Query("SELECT p FROM  RegistroVisitantePortariaEntity p WHERE p.filialSolicitado=:filial ORDER BY p.dataCriacao DESC")
    Page<RegistroVisitantePortariaEntity> findAllByFilial(@Param("filial") Integer filial, Pageable pageable);

    Optional<RegistroVisitantePortariaEntity> findTopByOrderByProtocoloDesc();
    Page<RegistroVisitantePortariaEntity> findAllByCriador_idOrderByDataCriacaoDesc(Pageable page,Long usuarioID);

    List<RegistroVisitantePortariaEntity> findAllByStatus(StatusPortaria status);
    @Query("""
    SELECT p 
    FROM RegistroVisitantePortariaEntity p
    WHERE p.ativo = TRUE 
      AND (p.filialSolicitado = :filial AND COALESCE(p.visitante.bloqueioAcesso, FALSE) = FALSE  OR p.visitante.acessoGlobal = TRUE)
    ORDER BY
        CASE WHEN p.prioridadeAtrasoAtivo = TRUE THEN 1 ELSE 0 END DESC,
        CASE WHEN p.status = 'AGUARDANDO_ENTRADA' THEN 1 ELSE 0 END DESC,
        p.dataCriacao DESC
    """)
    Page<RegistroVisitantePortariaEntity> findAllByFilialStatus(Integer filial, Pageable pageable,@Param("AGUARDANDO_ENTRADA") StatusPortaria aguardandoEntrada);
    @Query("SELECT p FROM RegistroVisitantePortariaEntity p WHERE p.placaVeiculo =:s and p.ativo = true")
    RegistroVisitantePortariaEntity findAllByPlacaVeiculo(@NotBlank String s);

    @Query("""
            SELECT p FROM RegistroVisitantePortariaEntity p 
            WHERE (p.filialSolicitado = :filial OR p.visitante.acessoGlobal = true )
            AND (
            LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.placaVeiculo) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.protocolo) LIKE LOWER(CONCAT('%', :busca, '%'))  
            )
            AND p.ativo = true 
            AND COALESCE(p.visitante.bloqueioAcesso, FALSE) = FALSE
             ORDER BY p.dataCriacao DESC
            """)
    Page<RegistroVisitantePortariaEntity> findAllByFilialAndBusca(Integer filial, Pageable pageable,@Param("busca") String busca);

    @Query("""
            SELECT p FROM  RegistroVisitantePortariaEntity p WHERE p.placaVeiculo = :placa 
            AND p.ativo = true 
             AND COALESCE(p.visitante.bloqueioAcesso, FALSE) = FALSE
            """)
    Optional<RegistroVisitantePortariaEntity> findByPlacaVeiculoAndAtivo(String placa);

    @Query("""
            SELECT p FROM  RegistroVisitantePortariaEntity p WHERE p.visitante.numeroTelefone = :numero
            AND p.ativo = true 
             AND COALESCE(p.visitante.bloqueioAcesso, FALSE) = FALSE
            """)
    Optional<RegistroVisitantePortariaEntity> findByVisitanteNumeroTelefoneAndAtivo(String numero);
    @Query("""
            SELECT p FROM RegistroVisitantePortariaEntity p 
            WHERE p.criador.id = :usuarioID 
            AND (
            LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.placaVeiculo) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.protocolo) LIKE LOWER(CONCAT('%', :busca, '%'))
            ) ORDER BY p.dataCriacao DESC
            """)
    Page<RegistroVisitantePortariaEntity> registrosUsuarios(Pageable page, Long usuarioID,@Param("busca") String busca);

    @Query("""
    SELECT r
    FROM RegistroVisitantePortariaEntity r
    LEFT JOIN FETCH r.visitante v
    LEFT JOIN FETCH v.recorrencia
    WHERE r.ativo = true
    """)
    List<RegistroVisitantePortariaEntity> findAllByAtivoTrue();
    @Query("""
            SELECT p FROM RegistroVisitantePortariaEntity p 
            WHERE (p.filialSolicitado = :filial OR p.visitante.acessoGlobal = true  )
            AND p.status = :status
            AND p.ativo = true 
            AND COALESCE(p.visitante.bloqueioAcesso, FALSE) = FALSE
            ORDER BY p.dataCriacao DESC
            """)
    Page<RegistroVisitantePortariaEntity> findAllByFilialAndBuscaStatusSalvo(Integer filial, Pageable pageable, StatusPortaria status);

    List<RegistroVisitantePortariaEntity> findAllByVisitante(VisitanteEntity visitante);


//    @Modifying
//    @Query("""
//            UPDATE RegistroVisitantePortariaEntity p
//            SET p.nomeCompleto = :#{#update.nomeCompleto},
//            p.placaVeiculo=:#{#update.placaVeiculo},
//            p.visitante.numeroTelefone = :#{#update.numeroTelefone}
//            WHERE p.id = :#{#update.id}
//
//            """)
//    void atualizarEntrada(AtualizaRegistro update);
}