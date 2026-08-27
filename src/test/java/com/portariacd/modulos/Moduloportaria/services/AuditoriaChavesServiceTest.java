package com.portariacd.modulos.Moduloportaria.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditoriaChavesServiceTest {
    private final LogAuditoriaChavesRepository repository = mock(LogAuditoriaChavesRepository.class);
    private final AuditoriaChavesService service = new AuditoriaChavesService(repository, objectMapper());

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveUsarUsuarioAutenticadoComoResponsavel() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(15L);
        usuario.setNome("Fiscal Teste");
        usuario.setFilial(116);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario, null, java.util.List.of()));

        service.registrar(AcaoAuditoriaChaves.CRIAR, ModuloAuditoriaChaves.CHAVE,
                "BlocoChaves", 1L, "Criou chave", null, null, 10L, 1L,
                null, Map.of("numero", 1), null, null);

        ArgumentCaptor<LogAuditoriaChavesEntity> captor = ArgumentCaptor.forClass(LogAuditoriaChavesEntity.class);
        verify(repository).save(captor.capture());
        LogAuditoriaChavesEntity log = captor.getValue();

        assertThat(log.getUsuarioResponsavelId()).isEqualTo(15L);
        assertThat(log.getUsuarioResponsavelNome()).isEqualTo("Fiscal Teste");
        assertThat(log.getFilial()).isEqualTo(116L);
    }

    @Test
    void deveRegistrarSistemaQuandoNaoExisteUsuarioAutenticado() {
        service.registrar(AcaoAuditoriaChaves.CRIAR_BLOCO, ModuloAuditoriaChaves.BLOCO,
                "BlocoEntity", 1L, "Criou bloco", null, 1L, null, null,
                null, Map.of("nome", "Secos"), null, null);

        ArgumentCaptor<LogAuditoriaChavesEntity> captor = ArgumentCaptor.forClass(LogAuditoriaChavesEntity.class);
        verify(repository).save(captor.capture());

        assertThat(captor.getValue().getUsuarioResponsavelId()).isNull();
        assertThat(captor.getValue().getUsuarioResponsavelNome()).isEqualTo("SISTEMA");
    }

    @Test
    void deveRemoverCamposSensiveisDosSnapshots() {
        service.registrar(AcaoAuditoriaChaves.EDITAR, ModuloAuditoriaChaves.USUARIO,
                "UsuarioEntity", 1L, "Editou usuário", 116L, null, null, null,
                Map.of("nome", "Joao", "password", "123", "token", "jwt"),
                Map.of("nome", "Joao Silva", "biometriaFacial", Map.of("embedding", "abc")),
                null, null);

        ArgumentCaptor<LogAuditoriaChavesEntity> captor = ArgumentCaptor.forClass(LogAuditoriaChavesEntity.class);
        verify(repository).save(captor.capture());
        LogAuditoriaChavesEntity log = captor.getValue();

        assertThat(log.getValorAnterior()).containsEntry("nome", "Joao");
        assertThat(log.getValorAnterior()).doesNotContainKeys("password", "token");
        assertThat(log.getValorNovo()).containsEntry("nome", "Joao Silva");
        assertThat(log.getValorNovo()).doesNotContainKey("biometriaFacial");
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
