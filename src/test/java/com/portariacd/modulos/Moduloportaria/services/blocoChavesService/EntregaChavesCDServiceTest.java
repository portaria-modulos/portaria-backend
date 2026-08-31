package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.DevolucaoChaves;
import com.portariacd.modulos.Moduloportaria.controllers.controleChaves.EntregaToken;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChaveHistory;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChaveHistoryRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChaveRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ChaveDevolucao;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.DesvolucaoChaveDto;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.ArmarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.BlocoChavesRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.UsuarioConsumerRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.exeption.UsuarioConsumerValidation;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EntregaChavesCDServiceTest {
    private final EntregaChaveRepository entregaRepository = mock(EntregaChaveRepository.class);
    private final UsuarioConsumerRepository usuarioConsumerRepository = mock(UsuarioConsumerRepository.class);
    private final ArmarioRepository armarioRepository = mock(ArmarioRepository.class);
    private final BlocoChavesRepository chavesRepository = mock(BlocoChavesRepository.class);
    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final EntregaChaveHistoryRepository historyRepository = mock(EntregaChaveHistoryRepository.class);
    private final AuditoriaChavesService auditoriaChavesService = mock(AuditoriaChavesService.class);
    private final EntregaChavesCDService service = new EntregaChavesCDService();

    private Armario armario;
    private BlocoChaves chave;
    private UsuarioConsumerChaves colaborador;

    @BeforeEach
    void configurar() {
        ReflectionTestUtils.setField(service, "repository", entregaRepository);
        ReflectionTestUtils.setField(service, "usuarioConsumerRepository", usuarioConsumerRepository);
        ReflectionTestUtils.setField(service, "armarioRepository", armarioRepository);
        ReflectionTestUtils.setField(service, "chavesRepository", chavesRepository);
        ReflectionTestUtils.setField(service, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(service, "entregaChaveHistoryRepository", historyRepository);
        ReflectionTestUtils.setField(service, "auditoriaChavesService", auditoriaChavesService);

        armario = new Armario();
        armario.setId(10L);
        armario.setFilial(116L);
        armario.setTipo(Tipo.PORTARIA_SECOS);

        chave = new BlocoChaves();
        chave.setId(20L);
        chave.setNumero(7);
        chave.setArmario(armario);
        chave.setAtivo(true);
        chave.setDisponivel(true);
        chave.setStatus(StatusArmario.LIVRE);

        colaborador = new UsuarioConsumerChaves();
        colaborador.setId(30L);
        colaborador.setMatricula("123");
        colaborador.setNome("Maria");
        colaborador.setEmpresa("GM");
        colaborador.setFilial(116);

        when(auditoriaChavesService.responsavelAtual())
                .thenReturn(new AuditoriaChavesService.ResponsavelAuditoria(40L, "Fiscal", 116L));
        when(auditoriaChavesService.snapshotChave(any())).thenReturn(Map.of("tipo", "chave"));
        when(auditoriaChavesService.snapshotEntrega(any())).thenReturn(Map.of("tipo", "entrega"));
        when(auditoriaChavesService.snapshotUsuarioConsumer(any())).thenReturn(Map.of("tipo", "usuario"));
    }

    @Test
    void deveEntregarChaveUsandoBuscaDiretaPorArmarioENumero() {
        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));
        when(entregaRepository.findEntregaAtiva(20L)).thenReturn(Optional.empty());
        when(usuarioConsumerRepository.findById(30L)).thenReturn(Optional.of(colaborador));
        when(entregaRepository.findEntregaUsuario("123", 10L)).thenReturn(Optional.empty());
        when(entregaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(chavesRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = service.entregaDeChaves(new EntregaChavesDTO(10L, 7, 30L, 40L));

        assertThat(resposta).isInstanceOf(EntregaToken.class);
        assertThat(chave.isDisponivel()).isFalse();
        assertThat(chave.getStatus()).isEqualTo(StatusArmario.OCUPADO);
        assertThat(chave.getUsuarioOcupacao()).isEqualTo("Maria");
        verify(chavesRepository).findByArmarioIdAndNumeroForUpdate(10L, 7);
        verify(armarioRepository, never()).findById(10L);
        verify(entregaRepository).save(any(EntregaChave.class));
        verify(historyRepository).save(any(EntregaChaveHistory.class));
        verify(auditoriaChavesService).registrar(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void deveBloquearEntregaQuandoChaveJaTemEntregaAtiva() {
        var entregaAtiva = new EntregaChave();
        entregaAtiva.setNomeColaborador("Joao");

        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));
        when(entregaRepository.findEntregaAtiva(20L)).thenReturn(Optional.of(entregaAtiva));

        assertThatThrownBy(() -> service.entregaDeChaves(new EntregaChavesDTO(10L, 7, 30L, 40L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Chave já está em uso");

        verify(entregaRepository, never()).save(any());
        verify(chavesRepository, never()).save(any());
    }

    @Test
    void deveBloquearEntregaQuandoStatusImpedeUso() {
        chave.setStatus(StatusArmario.BLOQUEADO);

        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));

        assertThatThrownBy(() -> service.entregaDeChaves(new EntregaChavesDTO(10L, 7, 30L, 40L)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Não é possível entregar");

        verify(entregaRepository, never()).save(any());
    }

    @Test
    void deveBloquearEntregaQuandoColaboradorForDeOutraFilial() {
        colaborador.setFilial(117);

        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));
        when(entregaRepository.findEntregaAtiva(20L)).thenReturn(Optional.empty());
        when(usuarioConsumerRepository.findById(30L)).thenReturn(Optional.of(colaborador));

        assertThatThrownBy(() -> service.entregaDeChaves(new EntregaChavesDTO(10L, 7, 30L, 40L)))
                .isInstanceOf(UsuarioConsumerValidation.class)
                .hasMessageContaining("Colaborador Diferente da filial");

        verify(entregaRepository, never()).save(any());
    }

    @Test
    void deveDevolverChaveUsandoBuscaDiretaPorArmarioENumero() {
        chave.setDisponivel(false);
        chave.setStatus(StatusArmario.OCUPADO);
        chave.setUsuarioOcupacao("Maria");
        chave.setUsuarioOcupacaoId(30L);

        var entregaAtiva = new EntregaChave();
        entregaAtiva.setBlocoChaves(chave);

        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));
        when(entregaRepository.findEntregaAtiva(20L)).thenReturn(Optional.of(entregaAtiva));
        when(entregaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(chavesRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resposta = service.liberacaoDeChaves(new DesvolucaoChaveDto(40L, new ChaveDevolucao(7, 10L)));

        assertThat(resposta).isInstanceOf(DevolucaoChaves.class);
        assertThat(chave.isDisponivel()).isTrue();
        assertThat(chave.getStatus()).isEqualTo(StatusArmario.LIVRE);
        assertThat(chave.getUsuarioOcupacao()).isNull();
        assertThat(entregaAtiva.getEntregue()).isTrue();
        verify(chavesRepository).findByArmarioIdAndNumeroForUpdate(10L, 7);
        verify(armarioRepository, never()).findById(10L);
        verify(entregaRepository).save(entregaAtiva);
        verify(historyRepository).save(any(EntregaChaveHistory.class));
    }

    @Test
    void deveFalharAoDevolverChaveSemEntregaAtiva() {
        when(chavesRepository.findByArmarioIdAndNumeroForUpdate(10L, 7)).thenReturn(Optional.of(chave));
        when(entregaRepository.findEntregaAtiva(20L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.liberacaoDeChaves(new DesvolucaoChaveDto(40L, new ChaveDevolucao(7, 10L))))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Informe chave em devolução");

        verify(entregaRepository, never()).save(any());
        verify(chavesRepository, never()).save(any());
    }
}
