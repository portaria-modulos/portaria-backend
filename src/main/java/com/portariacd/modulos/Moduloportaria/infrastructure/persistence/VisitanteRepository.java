package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.registroVisitante.RegistroVisitantePortariaEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VisitanteRepository extends JpaRepository<VisitanteEntity,Long>, JpaSpecificationExecutor<VisitanteEntity> {

    @Query("select  p from VisitanteEntity p where p.numeroTelefone=:numero")
    Optional<VisitanteEntity> findByOneByNumeroTelefone(String numero);

    @Query("""
            select p from VisitanteEntity p where 
            LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%',:busca,'%'))
            OR LOWER(p.numeroTelefone) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.recorrencia.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
            OR LOWER(p.placaCarro) LIKE LOWER(CONCAT('%', :busca, '%'))
            
            """)
    Page<VisitanteEntity> findbyBusca(Pageable page,@Param("busca") String busca);

    Page<VisitanteEntity> findAllByAtivoTrue(Pageable page);

    @Query("select  p from VisitanteEntity p where p.numeroTelefone=:s1")
    Optional<VisitanteEntity> findByNumeroTelefone(@NotBlank String s1);

    @Query("select  p from VisitanteEntity p where p.placaCarro=:placa")
    VisitanteEntity findByPlaca(String placa);
    @Query("select  p from VisitanteEntity p where p.id=:visitanteId OR LOWER(p.nomeCompleto) = LOWER(:nome)")
    VisitanteEntity findByIdByNome(Long visitanteId, String nome);

    Optional<VisitanteEntity> findFirstByIdOrNomeCompletoIgnoreCase(Long visitanteId, String nome);
}
