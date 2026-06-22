package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import com.portariacd.modulos.Moduloportaria.domain.gateways.BlocoInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.gateways.FilialInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.gateways.RecorrenciaInterfaceGateway;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.CadastroUsuarioDto;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.SistemaAcessoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.bloco.RegistroBlocoDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.recorrencia.RegistroRecorrenciaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.SistemaStatusEnun;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.*;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.blocos.BlocoEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.PermissionEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.PermissionRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.funcao.SistemaAcesso;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.recorrencia.Recorrencia;
import com.portariacd.modulos.Moduloportaria.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class DataInitializer implements CommandLineRunner {
    private final PerfilRepository perfilRepository;
    private final PermissionRepository permissionRepository;
    private final BlocoInterfaceGateway repository;
    private final RecorrenciaInterfaceGateway recorrencia;
    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private RecorrenciaRepository recorrenciaRepository;
    private FilialInterfaceGateway FilialGatewayRepository;
    private RespositorySistemaAcesso sistemaRepository;
    BlocoRepository blocoRepository;
    FilialRepository filialRepository;
    public DataInitializer(PerfilRepository perfilRepository,
                           PermissionRepository permissionRepository,
                           UsuarioService service,
                           UsuarioRepository usuarioRepository,
                           BlocoInterfaceGateway repository,
                           BlocoRepository blocoRepository,
                           RecorrenciaInterfaceGateway recorrencia,
                           RecorrenciaRepository recorrenciaRepository,
                           FilialInterfaceGateway FilialGatewayRepository,
                           FilialRepository filialRepository,
                           RespositorySistemaAcesso sistemaRepository
    ) {
        this.perfilRepository = perfilRepository;
        this.permissionRepository = permissionRepository;
        this.usuarioService = service;
        this.usuarioRepository = usuarioRepository;
        this.repository = repository;
        this.blocoRepository = blocoRepository;
        this.recorrencia = recorrencia;
        this.recorrenciaRepository = recorrenciaRepository;
        this.FilialGatewayRepository = FilialGatewayRepository;
        this.filialRepository = filialRepository;
        this.sistemaRepository = sistemaRepository;
    }
    @Override
    @Transactional
    public void run(String... args) {
        criarPermissoes();
        criarPerfis();
        criarUsuario();
        save();
        criarRecorrencia();
        CriarModulos();
    }
    private void criarPermissoes() {
        List<String> permissoesDesejadas = List.of(
                "CRIAR_REGISTRO", "EDITAR_REGISTRO", "DELETAR_REGISTRO", "VISUALIZAR_REGISTRO",
                "GERENCIAR_USUARIOS", "REGISTRAR_ENTRADA", "REGISTRAR_SAIDA","DELETAR_ENTRADA","EDITAR_ENTRADA",
                "GERAR_RELATORIO", "VISUALIZAR_VISITANTES", "ALTERAR_CONFIGURACOES","GERENCIAR_REGISTROS","DELETAR_USUARIO",
                "EDITAR_USUARIO","DELETE_GLOBAL","REGISTRO_CRIADO","LISTA_GERAL","CADASTRO_USUARIO","ADICIONAR_ACESSO",
                "DELETE_LOGISTICO"
        );
        Set<String> existentes = new HashSet<>(permissionRepository.findAll()
                .stream()
                .map(PermissionEntity::getName)
                .toList());

        for (String nome : permissoesDesejadas) {
            if (!existentes.contains(nome)) {
                PermissionEntity p = new PermissionEntity();
                p.setName(nome);
                p.setAtivo(true);
                permissionRepository.save(p);
            }
        }
    }
    private void criarPerfis() {
        Map<String, PermissionEntity> permissoesMap = new HashMap<>();
        for (PermissionEntity p : permissionRepository.findAll()) {
            permissoesMap.put(p.getName(), p);
        }
        // === ADMIN ===
        if (!perfilRepository.existsByNome("ADMIN")) {
            PerfilEntity admin = new PerfilEntity();
            admin.setNome("ADMIN");
            perfilRepository.saveAndFlush(admin);
            admin.setPermissoes(new HashSet<>(Arrays.asList(
                    permissoesMap.get("CRIAR_REGISTRO"),
                    permissoesMap.get("EDITAR_REGISTRO"),
                    permissoesMap.get("DELETAR_REGISTRO"),
                    permissoesMap.get("VISUALIZAR_REGISTRO"),
                    permissoesMap.get("GERENCIAR_USUARIOS"),
                    permissoesMap.get("REGISTRAR_ENTRADA"),
                    permissoesMap.get("REGISTRAR_SAIDA"),
                    permissoesMap.get("DELETE_LOGISTICO"),
                    permissoesMap.get("DELETAR_ENTRADA"),
                    permissoesMap.get("EDITAR_ENTRADA"),
                    permissoesMap.get("GERAR_RELATORIO"),
                    permissoesMap.get("VISUALIZAR_VISITANTES"),
                    permissoesMap.get("ALTERAR_CONFIGURACOES"),
                    permissoesMap.get("GERENCIAR_REGISTROS"),
                    permissoesMap.get("DELETAR_USUARIO"),
                    permissoesMap.get("EDITAR_USUARIO"),
                    permissoesMap.get("DELETE_GLOBAL"),
                    permissoesMap.get("REGISTRO_CRIADO"),
                    permissoesMap.get("CADASTRO_USUARIO"),
                    permissoesMap.get("ADICIONAR_ACESSO"),
                    permissoesMap.get("LISTA_GERAL"),
                    permissoesMap.get("DELETE_LOGISTICO")
            )));            perfilRepository.save(admin);
        }
        if (!perfilRepository.existsByNome("LOGISTICO_CONTROLE")) {
            PerfilEntity admin = new PerfilEntity();
            admin.setNome("LOGISTICO_CONTROLE");
            perfilRepository.saveAndFlush(admin);
            admin.setPermissoes(new HashSet<>(Arrays.asList(
                    permissoesMap.get("CRIAR_REGISTRO"),
                    permissoesMap.get("EDITAR_REGISTRO"),
                    permissoesMap.get("VISUALIZAR_REGISTRO"),
                   permissoesMap.get("DELETE_LOGISTICO")
            )));            perfilRepository.save(admin);
        }
        if (!perfilRepository.existsByNome("GERENTE_PREVENCAO")) {
            PerfilEntity gerente = new PerfilEntity();
            gerente.setNome("GERENTE_PREVENCAO");
            perfilRepository.saveAndFlush(gerente);

            gerente.setPermissoes(new HashSet<>(Arrays.asList(
                    permissoesMap.get("CRIAR_REGISTRO"),
                    permissoesMap.get("VISUALIZAR_REGISTRO"),

                    permissoesMap.get("GERENCIAR_USUARIOS"),
                    permissoesMap.get("GERAR_RELATORIO"),
                    permissoesMap.get("GERENCIAR_REGISTROS"),

                    permissoesMap.get("REGISTRAR_ENTRADA"),
                    permissoesMap.get("REGISTRAR_SAIDA"),
                    permissoesMap.get("VISUALIZAR_VISITANTES"),

                    permissoesMap.get("REGISTRO_CRIADO"),
                    permissoesMap.get("LISTA_GERAL")
            )));

            perfilRepository.save(gerente);
        }

        // === FISCAL ===
        if (!perfilRepository.existsByNome("FISCAL")) {
            PerfilEntity fiscal = new PerfilEntity();
            fiscal.setNome("FISCAL");
            perfilRepository.saveAndFlush(fiscal);
            fiscal.setPermissoes(
                    new HashSet<>(Arrays.asList(
                            permissoesMap.get("REGISTRAR_ENTRADA"),
                            permissoesMap.get("REGISTRAR_SAIDA"),
                            permissoesMap.get("VISUALIZAR_REGISTRO"),
                            permissoesMap.get("VISUALIZAR_VISITANTES"),
                            permissoesMap.get("REGISTRO_CRIADO")

                            )));
            perfilRepository.save(fiscal);
        }
        // === AUTORIZADO ===
        if (!perfilRepository.existsByNome("AUTORIZADOR")) {
            PerfilEntity autorizado = new PerfilEntity();
            autorizado.setNome("AUTORIZADOR");
            perfilRepository.saveAndFlush(autorizado);
            autorizado.setPermissoes(
            new HashSet<>(Arrays.asList(
                    permissoesMap.get("VISUALIZAR_REGISTRO"),
                    permissoesMap.get("EDITAR_REGISTRO"),
                    permissoesMap.get("CRIAR_REGISTRO"),
                    permissoesMap.get("REGISTRO_CRIADO"),
                    permissoesMap.get("DELETE_LOGISTICO")

                    ))
            );
            perfilRepository.save(autorizado);
        }
    }
    private void criarUsuario(){
        var usuario = new CadastroUsuarioDto("Grupomateus","admin@grupomateus.com.br","@Admin2025@#$","SUPORTE",116, 1l,1L);
       var usuarioEntity = usuarioRepository.findOneByEmail(usuario.email());
        if(usuarioEntity.isEmpty()) {
            usuarioService.registroUsuario(usuario);
        }
    }
    private void save(){
        Set<String> existentes = new HashSet<>(blocoRepository.findAll()
                .stream()
                .map(BlocoEntity::getNome)
                .toList());
        Set<RegistroBlocoDTO> blocos = Set.of(
                new RegistroBlocoDTO("Secos"),
                new RegistroBlocoDTO("Hortifrúti"),
                new RegistroBlocoDTO("Frios"),
                new RegistroBlocoDTO("Fatiados"),
                new RegistroBlocoDTO("Indústria"),
                new RegistroBlocoDTO("Spazio"),
                new RegistroBlocoDTO("Oficina Caminhões"),
                new RegistroBlocoDTO("Material Logistico")
        );
        for(RegistroBlocoDTO e:blocos){
             if(!existentes.contains(e.nome())){
                 repository.registroBloco(e);
             }
        }
    }
    private void criarRecorrencia(){
        Set<String> existentes = new HashSet<>(recorrenciaRepository.findAll()
                .stream()
                .map(Recorrencia::getNome)
                .toList());
        Set<RegistroRecorrenciaDTO> recorrencias = Set.of(
                new RegistroRecorrenciaDTO("RECORRENTE"),
                new RegistroRecorrenciaDTO("UNICO"),
                new RegistroRecorrenciaDTO("RECORRENTE TEMPORARIO")
        );
        for(RegistroRecorrenciaDTO e:recorrencias){
            if(!existentes.contains(e.nome())){
                recorrencia.registroRecorrencia(e);
            }
        }
    }
//    private void criarFiliais(){
//        Set<String> existentes = new HashSet<>(filialRepository.findAll()
//                .stream()
//                .map(Filial::getNome)
//                .toList());
//        Set<RegistroFilialDTO> recorrencias = Set.of(
//                new RegistroFilialDTO("CD87-DAVINOPOLIS-MA",87),
//                new RegistroFilialDTO("CD116-ITAPERA-MA",116)
//                );
//        for(RegistroFilialDTO e:recorrencias){
//            if(!existentes.contains(e.nome())){
//                FilialGatewayRepository.registroFilial(e);
//            }
//        }
//    }
    private void CriarModulos(){
        Set<String> existentes = new HashSet<>(sistemaRepository.findAll()
                .stream()
                .map(SistemaAcesso::getTitulo)
                .toList());
        Set<SistemaAcessoDTO> re= Set.of(
                new SistemaAcessoDTO("Portaria","Gerenciamento de permissões e módulos do usuário","/portaria",true, SistemaStatusEnun.PORTARIA_ACCESS.name()),
                new SistemaAcessoDTO("Recebimento","","/recebimento",true,SistemaStatusEnun.RECEBIMENTO_ACCESS.name()),
                new SistemaAcessoDTO("Inventário","Consulta e resultado de inventário","/inventario",true,SistemaStatusEnun.INVENTARIO_ACCESS.name()),
        new SistemaAcessoDTO("Material Logistico","Controle de material Logistico","/logistico",true,SistemaStatusEnun.LOGISTICO_ACCESS.name())

        );
        for(SistemaAcessoDTO e:re){
            if(!existentes.contains(e.titulo())){
                sistemaRepository.save(new SistemaAcesso(e));
            }
        }
    }
}
