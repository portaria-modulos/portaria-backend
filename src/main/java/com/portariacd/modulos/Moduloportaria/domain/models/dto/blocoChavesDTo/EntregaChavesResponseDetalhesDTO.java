package com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

public record EntregaChavesResponseDetalhesDTO(
 String nomeColaborador,
 String matriculaColaborador,
 String empresaColaborador,
 String setor,
 OffsetDateTime dataHoraRetirada,
 LocalDateTime dataHoraDevolucao,
 Long filialId,
 Boolean disponivel,
 BlocoChavesResponseDTO armario
) {
    public EntregaChavesResponseDetalhesDTO(EntregaChave e) {
        this(e.getNomeColaborador(),
                e.getMatriculaColaborador()!=null?e.getMatriculaColaborador():null,
                e.getEmpresaColaborador(),
                null
                ,e.getDataHoraRetirada(),
                e.getDataHoraDevolucao(),
                e.getFilialId(),
                e.getEntregue(),
                new BlocoChavesResponseDTO(e.getBlocoChaves())
        );
    }

    public EntregaChavesResponseDetalhesDTO(BlocoChaves e, UsuarioConsumerChaves usuariosConsumer) {

        this(
                usuariosConsumer != null ? usuariosConsumer.getNome() : null,
                usuariosConsumer != null ? usuariosConsumer.getMatricula() : null,
                usuariosConsumer != null ? usuariosConsumer.getEmpresa() : null,
                usuariosConsumer != null ? usuariosConsumer.getSetor() : null,
                null,
                null,
                e.getArmario().getFilial(),
                e.isDisponivel(),
                new BlocoChavesResponseDTO(e)
        );
    }
}
