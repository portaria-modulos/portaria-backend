package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.domain.gateways.BlocoInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.AcaoAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.controleDeChaves.auditoria.ModuloAuditoriaChaves;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RegistroBlocoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RequestBlocoDTO;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.BlocoRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos.BlocoEntity;
import com.portariacd.modulos.Moduloportaria.services.AuditoriaChavesService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlocoAdpter  implements BlocoInterfaceGateway {
    private BlocoRepository repository;
    private AuditoriaChavesService auditoriaChavesService;

    public BlocoAdpter(BlocoRepository repository, AuditoriaChavesService auditoriaChavesService){
        this.repository = repository;
        this.auditoriaChavesService = auditoriaChavesService;
    }
    @Override
    @Transactional
    public void registroBloco(RegistroBlocoDTO registro){
            if (repository.findByNome(registro.nome()).isPresent()) {
                throw new RuntimeException("Nome '" + registro.nome() + "' já cadastrado");
            }
        BlocoEntity bloco = new BlocoEntity(registro.nome());
        var salvo = repository.save(bloco);
        auditoriaChavesService.registrar(
                AcaoAuditoriaChaves.CRIAR_BLOCO,
                ModuloAuditoriaChaves.BLOCO,
                "BlocoEntity",
                salvo.getId(),
                "Bloco %s criado.".formatted(salvo.getNome()),
                null,
                salvo.getId(),
                null,
                null,
                null,
                auditoriaChavesService.snapshotBloco(salvo),
                null,
                null
        );
    }
    @Override
    public List<RequestBlocoDTO> lista(){
       return repository.findAll().stream().map(RequestBlocoDTO::new).collect(Collectors.toList());
    }
}
