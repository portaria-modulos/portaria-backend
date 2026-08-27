package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.ArmarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.BlocoChavesRepository;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlocoChaveServicesTest {
    private final ArmarioRepository armarioRepository = mock(ArmarioRepository.class);
    private final BlocoChavesRepository chavesRepository = mock(BlocoChavesRepository.class);
    private final AuditoriaChavesService auditoriaChavesService = mock(AuditoriaChavesService.class);
    private final BlocoChaveServices service = new BlocoChaveServices();

    @BeforeEach
    void configurar() {
        ReflectionTestUtils.setField(service, "repository", armarioRepository);
        ReflectionTestUtils.setField(service, "chavesRepository", chavesRepository);
        ReflectionTestUtils.setField(service, "auditoriaChavesService", auditoriaChavesService);
    }

    @Test
    void deveAceitarQuantidade2500() {
        Armario armario = new Armario();
        armario.setId(10L);
        armario.setFilial(116L);
        armario.setTipo(Tipo.PORTARIA_SECOS);
        armario.setBlocoChaves(new ArrayList<>());

        when(armarioRepository.findById(10L)).thenReturn(Optional.of(armario));
        when(chavesRepository.buscarUltimoNumero(10L)).thenReturn(0);
        when(chavesRepository.countByArmarioId(10L)).thenReturn(0L);
        when(chavesRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = service.cadastrarChaves(new BlocoChavesDTO(10L, 2500));

        assertThat(resposta).hasSize(2500);
        verify(chavesRepository).saveAll(any());
    }

    @Test
    void deveRejeitarQuantidade2501SemSalvarParcial() {
        assertThatThrownBy(() -> service.cadastrarChaves(new BlocoChavesDTO(10L, 2501)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Quantidade de chaves deve estar entre 1 e 2500");

        verify(chavesRepository, never()).saveAll(any(List.class));
    }

    @Test
    void deveRejeitarQuantidadeZeroSemSalvarParcial() {
        assertThatThrownBy(() -> service.cadastrarChaves(new BlocoChavesDTO(10L, 0)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Quantidade de chaves deve estar entre 1 e 2500");

        verify(chavesRepository, never()).saveAll(any(List.class));
    }
}
