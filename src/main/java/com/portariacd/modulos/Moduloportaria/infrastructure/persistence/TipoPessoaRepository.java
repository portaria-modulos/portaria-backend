package com.portariacd.modulos.Moduloportaria.infrastructure.persistence;

import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.tipoPessoa.TipoPessoaEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoPessoaRepository extends JpaRepository<TipoPessoaEntity,Long> {
    Optional<TipoPessoaEntity> findByNome(@NotBlank String nome);

    List<TipoPessoaEntity> findAllByOrderByIdAsc();

}
