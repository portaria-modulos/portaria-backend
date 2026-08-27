package com.portariacd.modulos.Moduloportaria.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.EntregaChave;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.UsuarioConsumerChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.controleChaves.auditoria.LogAuditoriaChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.adapters.buscaLog.LogAuditoriaChavesSpec;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.auditoria.LogAuditoriaChavesRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos.BlocoEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AuditoriaChavesService {
    private static final Set<String> CAMPOS_SENSIVEIS = Set.of(
            "password", "senha", "token", "jwt", "refreshToken", "refresh_token",
            "credentials", "credenciais", "biometriaFacial", "embedding", "imagem", "avatar"
    );

    private final LogAuditoriaChavesRepository repository;
    private final ObjectMapper objectMapper;

    public AuditoriaChavesService(LogAuditoriaChavesRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void registrar(AcaoAuditoriaChaves acao,
                          ModuloAuditoriaChaves modulo,
                          String entidade,
                          Long entidadeId,
                          String descricao,
                          Long filial,
                          Long blocoId,
                          Long armarioId,
                          Long chaveId,
                          Object valorAnterior,
                          Object valorNovo,
                          Integer quantidadeAnterior,
                          Integer quantidadeNova) {
        ResponsavelAuditoria responsavel = responsavelAtual();
        HttpServletRequest request = requestAtual();

        LogAuditoriaChavesEntity log = new LogAuditoriaChavesEntity();
        log.setDataHora(OffsetDateTime.now());
        log.setAcao(acao);
        log.setModulo(modulo);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setDescricao(descricao);
        log.setUsuarioResponsavelId(responsavel.id());
        log.setUsuarioResponsavelNome(responsavel.nome());
        log.setFilial(filial != null ? filial : responsavel.filial());
        log.setBlocoId(blocoId);
        log.setArmarioId(armarioId);
        log.setChaveId(chaveId);
        log.setValorAnterior(toMapSanitizado(valorAnterior));
        log.setValorNovo(toMapSanitizado(valorNovo));
        log.setQuantidadeAnterior(quantidadeAnterior);
        log.setQuantidadeNova(quantidadeNova);
        log.setEnderecoIp(request == null ? null : getClientIp(request));
        log.setUserAgent(request == null ? null : request.getHeader("User-Agent"));
        repository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<LogAuditoriaChavesResponseDTO> listar(Pageable pageable,
                                                       OffsetDateTime dataInicial,
                                                       OffsetDateTime dataFinal,
                                                       AcaoAuditoriaChaves acao,
                                                       ModuloAuditoriaChaves modulo,
                                                       String entidade,
                                                       Long entidadeId,
                                                       Long usuarioResponsavelId,
                                                       Long filial,
                                                       Long blocoId,
                                                       Long armarioId,
                                                       Long chaveId) {
        Pageable ordenado = pageable.isUnpaged()
                ? Pageable.unpaged(Sort.by(Sort.Direction.DESC, "dataHora"))
                : pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "dataHora"));
        Specification<LogAuditoriaChavesEntity> spec = Specification.allOf(
                LogAuditoriaChavesSpec.dataInicial(dataInicial),
                LogAuditoriaChavesSpec.dataFinal(dataFinal),
                LogAuditoriaChavesSpec.acao(acao),
                LogAuditoriaChavesSpec.modulo(modulo),
                LogAuditoriaChavesSpec.entidade(entidade),
                LogAuditoriaChavesSpec.entidadeId(entidadeId),
                LogAuditoriaChavesSpec.usuarioResponsavelId(usuarioResponsavelId),
                LogAuditoriaChavesSpec.filial(filial),
                LogAuditoriaChavesSpec.blocoId(blocoId),
                LogAuditoriaChavesSpec.armarioId(armarioId),
                LogAuditoriaChavesSpec.chaveId(chaveId)
        );
        return repository.findAll(spec, ordenado).map(LogAuditoriaChavesResponseDTO::new);
    }

    public ResponsavelAuditoria responsavelAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UsuarioEntity usuario)) {
            return new ResponsavelAuditoria(null, "SISTEMA", null);
        }
        return new ResponsavelAuditoria(usuario.getId(), usuario.getNome(), (long) usuario.getFilial());
    }

    public Map<String, Object> snapshotArmario(Armario armario) {
        if (armario == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", armario.getId());
        snapshot.put("filial", armario.getFilial());
        snapshot.put("tipo", armario.getTipo() == null ? null : armario.getTipo().name());
        snapshot.put("quantidadeChaves", armario.getBlocoChaves() == null ? 0 : armario.getBlocoChaves().size());
        return snapshot;
    }

    public Map<String, Object> snapshotChave(BlocoChaves chave) {
        if (chave == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", chave.getId());
        snapshot.put("numero", chave.getNumero());
        snapshot.put("ativo", chave.isAtivo());
        snapshot.put("disponivel", chave.isDisponivel());
        snapshot.put("status", chave.getStatus() == null ? null : chave.getStatus().name());
        snapshot.put("descricaoProblema", chave.getDescricaoProblema());
        snapshot.put("usuarioOcupacao", chave.getUsuarioOcupacao());
        snapshot.put("usuarioOcupacaoId", chave.getUsuarioOcupacaoId());
        if (chave.getArmario() != null) {
            snapshot.put("armarioId", chave.getArmario().getId());
            snapshot.put("filial", chave.getArmario().getFilial());
            snapshot.put("tipoArmario", chave.getArmario().getTipo() == null ? null : chave.getArmario().getTipo().name());
        }
        return snapshot;
    }

    public Map<String, Object> snapshotUsuarioConsumer(UsuarioConsumerChaves usuario) {
        if (usuario == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", usuario.getId());
        snapshot.put("matricula", usuario.getMatricula());
        snapshot.put("gmcoreId", usuario.getGmcoreId());
        snapshot.put("cpf", usuario.getCpf());
        snapshot.put("nome", usuario.getNome());
        snapshot.put("setor", usuario.getSetor());
        snapshot.put("filial", usuario.getFilial());
        snapshot.put("ativo", usuario.getAtivo());
        snapshot.put("empresa", usuario.getEmpresa());
        snapshot.put("tipoColaboradorFuncionario", usuario.getTipoColaboradorFuncionario());
        snapshot.put("tipoFuncionario", usuario.getTipoFuncionario());
        return snapshot;
    }

    public Map<String, Object> snapshotUsuarioSistema(UsuarioEntity usuario) {
        if (usuario == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", usuario.getId());
        snapshot.put("nome", usuario.getNome());
        snapshot.put("email", usuario.getEmail());
        snapshot.put("ocupacaoOperacional", usuario.getOcupacaoOperacional());
        snapshot.put("filial", usuario.getFilial());
        snapshot.put("ativo", usuario.getAtivo());
        snapshot.put("perfilId", usuario.getPerfil() == null ? null : usuario.getPerfil().getId());
        snapshot.put("perfil", usuario.getPerfil() == null ? null : usuario.getPerfil().getNome());
        return snapshot;
    }

    public Map<String, Object> snapshotBloco(BlocoEntity bloco) {
        if (bloco == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", bloco.getId());
        snapshot.put("nome", bloco.getNome());
        snapshot.put("gmBlocoId", bloco.getGmBlocoId());
        return snapshot;
    }

    public Map<String, Object> snapshotEntrega(EntregaChave entrega) {
        if (entrega == null) return null;
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("id", entrega.getId());
        snapshot.put("nomeColaborador", entrega.getNomeColaborador());
        snapshot.put("matriculaColaborador", entrega.getMatriculaColaborador());
        snapshot.put("empresaColaborador", entrega.getEmpresaColaborador());
        snapshot.put("dataHoraRetirada", entrega.getDataHoraRetirada());
        snapshot.put("dataHoraDevolucao", entrega.getDataHoraDevolucao());
        snapshot.put("usuarioPortariaRetirada", entrega.getUsuarioPortariaRetirada());
        snapshot.put("usuarioIdRetirada", entrega.getUsuarioIdRetirada());
        snapshot.put("usuarioPortariaDevolucao", entrega.getUsuarioPortariaDevolucao());
        snapshot.put("usuarioIdDevolucao", entrega.getUsuarioIdDevolucao());
        snapshot.put("filialId", entrega.getFilialId());
        snapshot.put("entregue", entrega.getEntregue());
        if (entrega.getBlocoChaves() != null) {
            snapshot.put("chaveId", entrega.getBlocoChaves().getId());
            snapshot.put("numeroChave", entrega.getBlocoChaves().getNumero());
            snapshot.put("armarioId", entrega.getBlocoChaves().getArmario() == null ? null : entrega.getBlocoChaves().getArmario().getId());
        }
        return snapshot;
    }

    private Map<String, Object> toMapSanitizado(Object valor) {
        if (valor == null) return null;
        Map<String, Object> map = objectMapper.convertValue(valor, new TypeReference<>() {});
        return sanitizar(map);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> sanitizar(Map<String, Object> origem) {
        Map<String, Object> destino = new LinkedHashMap<>();
        origem.forEach((chave, valor) -> {
            if (isCampoSensivel(chave)) return;
            if (valor instanceof Map<?, ?> map) {
                destino.put(chave, sanitizar((Map<String, Object>) map));
            } else if (valor instanceof List<?> lista) {
                destino.put(chave, lista.stream()
                        .map(item -> item instanceof Map<?, ?> map ? sanitizar((Map<String, Object>) map) : item)
                        .toList());
            } else {
                destino.put(chave, valor);
            }
        });
        return destino;
    }

    private boolean isCampoSensivel(String campo) {
        String normalizado = campo == null ? "" : campo.trim();
        return CAMPOS_SENSIVEIS.stream().anyMatch(sensivel -> sensivel.equalsIgnoreCase(normalizado));
    }

    private HttpServletRequest requestAtual() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    public record ResponsavelAuditoria(Long id, String nome, Long filial) {
    }
}
