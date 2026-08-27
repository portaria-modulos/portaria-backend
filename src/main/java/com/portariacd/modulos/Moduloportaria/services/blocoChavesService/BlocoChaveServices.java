package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Armario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.BlocoChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.StatusArmario;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.Tipo;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.ArmarioDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.ArmarioResponseDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.blocoChavesDTo.BlocoChavesResponseDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.ArmarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.AreaPersisteAmario.BlocoChavesRepository;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BlocoChaveServices {
    public static final int LIMITE_MAXIMO_CRIACAO_CHAVES = 2500;

    @Autowired
   private ArmarioRepository repository;
    @Autowired
    private BlocoChavesRepository chavesRepository;
    @Autowired
    private AuditoriaChavesService auditoriaChavesService;

   @Transactional
   public void criaArmario(ArmarioDTO dto){
    Optional<Armario> response = repository.findByFilial(dto.filial(), Tipo.convertTipo(dto.tipo()));
    if(response.isPresent()){
        throw new RuntimeException("Ja contém armario criado");
    }
      Armario armario = repository.save(new Armario(dto));
      auditoriaChavesService.registrar(
              AcaoAuditoriaChaves.CRIAR_ARMARIO,
              ModuloAuditoriaChaves.ARMARIO,
              "Armario",
              armario.getId(),
              "Armário %s criado para filial %d.".formatted(armario.getTipo().name(), armario.getFilial()),
              armario.getFilial(),
              null,
              armario.getId(),
              null,
              null,
              auditoriaChavesService.snapshotArmario(armario),
              null,
              null
      );
   }

   public List<ArmarioResponseDTO> listarArmariosComProblema(Integer filial, String tipo) {
       Tipo tipoFiltro = tipo == null || tipo.isBlank() ? null : Tipo.convertTipo(tipo);
       return repository.findAll().stream()
               .filter(armario -> filial == null || filial.longValue() == armario.getFilial())
               .filter(armario -> tipoFiltro == null || armario.getTipo() == tipoFiltro)
               .filter(armario -> armario.getBlocoChaves().stream()
                       .anyMatch(chave -> isStatusProblema(chave.getStatus())))
               .map(armario -> new ArmarioResponseDTO(
                       armario.getId(),
                       armario.getFilial(),
                       armario.getTipo().name(),
                       armario.getBlocoChaves().stream()
                               .filter(chave -> isStatusProblema(chave.getStatus()))
                               .map(BlocoChavesResponseDTO::new)
                               .toList()))
               .toList();
   }

   private boolean isStatusProblema(StatusArmario status) {
       return status == StatusArmario.EM_MANUTENCAO || status == StatusArmario.BLOQUEADO;
   }

   @Transactional
   public BlocoChaves alterarStatusBloco(Long armarioId, Long blocoId, Integer numeroChave,
                                         String tipo, String status, String descricaoProblema) {
       var armario = repository.findById(armarioId)
               .orElseThrow(() -> new RuntimeException("Armário não encontrado"));
       validarTipo(armario, tipo);
       var bloco = armario.getBlocoChaves().stream()
               .filter(chave -> blocoId != null
                       ? chave.getId().equals(blocoId)
                       : chave.getNumero().equals(numeroChave))
               .findFirst()
               .orElseThrow(() -> new RuntimeException("Chave não encontrada no armário"));
       var snapshotAnterior = auditoriaChavesService.snapshotChave(bloco);
       var statusAnterior = bloco.getStatus();
       var ativoAnterior = bloco.isAtivo();
       StatusArmario novoStatus = converterStatus(status);
       bloco.setStatus(novoStatus);
       boolean ativo = novoStatus == StatusArmario.LIVRE || novoStatus == StatusArmario.OCUPADO;
       bloco.setAtivo(ativo);
       bloco.setDisponivel(novoStatus == StatusArmario.LIVRE);
       bloco.setDescricaoProblema(ativo ? null : descricaoProblema);
       var salvo = chavesRepository.save(bloco);
       AcaoAuditoriaChaves acao = acaoStatus(ativoAnterior, ativo);
       String descricao = "Status da chave %d do armário %d alterado de %s para %s."
               .formatted(salvo.getNumero(), armario.getId(), statusAnterior, novoStatus);
       auditoriaChavesService.registrar(
               acao,
               ModuloAuditoriaChaves.CHAVE,
               "BlocoChaves",
               salvo.getId(),
               descricao,
               armario.getFilial(),
               blocoId,
               armario.getId(),
               salvo.getId(),
               snapshotAnterior,
               auditoriaChavesService.snapshotChave(salvo),
               null,
               null
       );
       return salvo;
   }

   private AcaoAuditoriaChaves acaoStatus(boolean ativoAnterior, boolean ativoNovo) {
       if (!ativoAnterior && ativoNovo) return AcaoAuditoriaChaves.ATIVAR;
       if (ativoAnterior && !ativoNovo) return AcaoAuditoriaChaves.DESATIVAR;
       return AcaoAuditoriaChaves.ATUALIZAR_STATUS;
   }

   private void validarTipo(Armario armario, String tipo) {
       if (tipo != null && !tipo.isBlank()
               && armario.getTipo() != Tipo.convertTipo(tipo)) {
           throw new RuntimeException("O tipo informado não pertence ao armário");
       }
   }

   private StatusArmario converterStatus(String status) {
       try {
           return StatusArmario.valueOf(status.trim().toUpperCase());
       } catch (IllegalArgumentException e) {
           return StatusArmario.converteTipoArmario(status.trim());
       }
   }
    public List<ArmarioResponseDTO> visualizarArmarios() {
        return repository.findAll()
                .stream()
                .map(armario -> new ArmarioResponseDTO(
                        armario.getId(),
                        armario.getFilial(),
                        armario.getTipo().name(),
                        armario.getBlocoChaves()
                                .stream()
                                .sorted(Comparator.comparing(BlocoChaves::getNumero))
                                .map(BlocoChavesResponseDTO::new)
                                .toList()
                ))
                .toList();
    }
    
    @Transactional
    public List<BlocoChavesResponseDTO> cadastrarChaves(BlocoChavesDTO bloco) {
       validarQuantidade(bloco.quantidade());
       var armario = repository.findById(bloco.amarioId()).orElseThrow(
               ()-> new RuntimeException("Armarios não encontrado")
       );
        Integer ultimoNumero = chavesRepository.buscarUltimoNumero(bloco.amarioId());
        int quantidadeAnterior = Math.toIntExact(chavesRepository.countByArmarioId(bloco.amarioId()));
        var snapshotAnterior = auditoriaChavesService.snapshotArmario(armario);
        List<BlocoChaves> novosBlocos = new ArrayList<>();
        for (var i=1;i<=bloco.quantidade();i++){
            BlocoChaves chave = new BlocoChaves();
            chave.setNumero(ultimoNumero + i);
            chave.setArmario(armario);
            chave.setDisponivel(true);
            chave.setAtivo(true);
            chave.setStatus(StatusArmario.LIVRE);
            novosBlocos.add(chave);
        }
        var salvos = chavesRepository.saveAll(novosBlocos);
        int quantidadeFinal = quantidadeAnterior + salvos.size();
        auditoriaChavesService.registrar(
                AcaoAuditoriaChaves.ADICIONAR_CHAVES,
                ModuloAuditoriaChaves.CHAVE,
                "BlocoChaves",
                armario.getId(),
                "Adicionadas %d chaves ao armário %d. Quantidade anterior: %d. Quantidade final: %d."
                        .formatted(salvos.size(), armario.getId(), quantidadeAnterior, quantidadeFinal),
                armario.getFilial(),
                null,
                armario.getId(),
                null,
                snapshotAnterior,
                Map.of(
                        "armarioId", armario.getId(),
                        "filial", armario.getFilial(),
                        "tipo", armario.getTipo().name(),
                        "primeiraChave", salvos.isEmpty() ? null : salvos.get(0).getNumero(),
                        "ultimaChave", salvos.isEmpty() ? null : salvos.get(salvos.size() - 1).getNumero(),
                        "quantidadeAdicionada", salvos.size(),
                        "quantidadeFinal", quantidadeFinal
                ),
                quantidadeAnterior,
                quantidadeFinal
        );
        return salvos.stream()
                .map(BlocoChavesResponseDTO::new)
                .toList();

    }

    private void validarQuantidade(Integer quantidade) {
       if (quantidade == null || quantidade < 1 || quantidade > LIMITE_MAXIMO_CRIACAO_CHAVES) {
           throw new RuntimeException("Quantidade de chaves deve estar entre 1 e "
                   + LIMITE_MAXIMO_CRIACAO_CHAVES);
       }
    }
    @Cacheable(value = "armarios", key = "#filial + ':' + #tipo")
    public ArmarioResponseDTO unicoArmariodId(Long armarioId, Tipo tipo) {
        var armarios = repository.findAllArmario(armarioId,tipo);
        return new ArmarioResponseDTO(armarios);
    }
}
