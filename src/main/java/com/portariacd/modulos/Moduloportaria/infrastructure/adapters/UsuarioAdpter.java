package com.portariacd.modulos.Moduloportaria.infrastructure.adapters;

import com.portariacd.modulos.Moduloportaria.services.LogAcaoService;
import com.portariacd.modulos.Moduloportaria.domain.gateways.UsuarioGatewayRepository;
import com.portariacd.modulos.Moduloportaria.domain.models.auth.AlteraSenhaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.*;
import com.portariacd.modulos.Moduloportaria.infrastructure.config.TokenConfigure;
import com.portariacd.modulos.Moduloportaria.domain.models.auth.Usuario;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.CadastroUsuarioDto;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.PerfilEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.PerfilRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidaNomeImagem;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidaNovaPassword;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.Validator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class UsuarioAdpter implements UsuarioGatewayRepository {
    private final UsuarioRepository repository;
    private final PerfilRepository perfilRepository;
    private AuthenticationManager authenticationManager;
    private final TokenConfigure tokenConfigure;
    private final Validator validator;
    private final ValidaNovaPassword validaNovaPassword;
    @Value("${endpoint}")
    private String endpointUrl;
    private final LogAcaoService service;

    public UsuarioAdpter(UsuarioRepository repository,
                         PerfilRepository perfilRepository
                           , AuthenticationManager authenticationManager,
                         TokenConfigure tokenConfigure,
                         Validator validator,
                         ValidaNovaPassword validaNovaPassword,
                         LogAcaoService service
    ){
        this.repository = repository;
        this.tokenConfigure = tokenConfigure;
        this.perfilRepository = perfilRepository;
        this.authenticationManager = authenticationManager;
        this.validator = validator;
        this.validaNovaPassword = validaNovaPassword;
        this.service = service;
    }

    public void registroUsuario(CadastroUsuarioDto dto){
        validator.valid(dto.password());
       if(repository.findOneByEmail(dto.email()).isPresent()) {
           throw new RuntimeException("Usuario ja existe!");
       };
       PerfilEntity perfil =  perfilRepository.findById(dto.perfilId()).orElseThrow(
                ()-> new RuntimeException("Erro ao buscar Perfil"));

        Usuario usuario = new Usuario(dto, LocalDateTime.now(),true);

       var usuarioEntity = new UsuarioEntity(usuario,perfil);

        if (perfil.getUsuario() != null) {
            perfil.getUsuario().add(usuarioEntity);
        } else {
            perfil.setUsuario(new ArrayList<>());
            perfil.getUsuario().add(usuarioEntity);
        }

       var usuarioSalvo =  repository.save(usuarioEntity);
     var usuarioCadastro =  repository.findById(dto.usuarioLogado()).orElseThrow(
             RuntimeException::new
        );
     String msg = "Usuário cadastrado com sucesso (ID: %d) (NOME: %s) ".formatted(usuarioSalvo.getId(), usuarioSalvo.getNome());

        salvaLog(new UsuarioRequestDTO(usuarioCadastro),"CRIAR_USUARIO",msg);

    }
    public Page<UsuarioRequestDTO> listaUsuario(Pageable page,String busca){
        Page<UsuarioRequestDTO> pageLista;
       if(busca !=null && !busca.isEmpty()){
          pageLista = repository.findAllByUsuario(page,busca)
                   .map(UsuarioRequestDTO::new);
       }else {
           pageLista = repository.findAllByUsuarioRegistrado(page).map(UsuarioRequestDTO::new);
       }
        return pageLista;
    }
    @Override
    public TokenResponse authLogin(UsuarioLoginDTO dto) {
        var token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var entity = authenticationManager.authenticate(token);

        var usuario = entity.getPrincipal();

        var usuarioModel = new Usuario((UsuarioEntity) usuario);

        String sessionId = UUID.randomUUID().toString();

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(8);
        // 4 — IP do cliente
        String ip = dto.ip();
        UsuarioEntity usuarioLogado = repository.findOneByEmail(dto.email()).orElseThrow();
        usuarioLogado.setCurrentSession(sessionId);
        usuarioLogado.setSessionExpiresAt(expiresAt);
        usuarioLogado.setSessionDevice( detectDevice(dto.userAgent()));
        usuarioLogado.setSessionIp(ip);
        usuarioLogado.setUltimoNavegador(dto.agentenav());
        usuarioLogado.setSessionLastLogin(LocalDateTime.now());
        repository.save(usuarioLogado);
        String tokenGerado = tokenConfigure.geraToken(usuarioModel,sessionId);
        return new TokenResponse(tokenGerado,new usuarioRequestDTO(((UsuarioEntity) usuario).getId()));
    }

    @Override
    public Map<String, UsuarioRequestPerfilDTO> buscaUsuarioPerfil(String email) {
        Map<String, UsuarioRequestPerfilDTO> map = new HashMap<>();
    var response =  repository.findOneByEmail(email).orElseThrow(
                ()-> new RuntimeException("Usuario não existe!")
        );
     map.put("usuario",new UsuarioRequestPerfilDTO(response));

        return map;
    }
    private String detectDevice(String userAgent) {
        if (userAgent == null) return "unknown";

        String ua = userAgent.toLowerCase();

        if (ua.contains("iphone") || ua.contains("android"))
            return "mobile";
        if (ua.contains("ipad") || ua.contains("tablet"))
            return "tablet";
        return "desktop";
    }
    @Override
    public String adicionarPerfil(long usuarioId, Long perfilId,Boolean ativo) {
            String mensagem = null;
            var usuario =  repository.findById(usuarioId).orElseThrow(
                    ()-> new RuntimeException("Usuario não existe!")
            );

            if(perfilId!=null){
              if (!usuario.getAtivo()){
                  throw new RuntimeException("Não foi possivel adicionar o perfil: usuário está inativo.");
              }
                var perfil = perfilRepository.findById(perfilId).orElseThrow(
                        ()->  new EntityNotFoundException("Perfil não encontrado.")
                );
                usuario.setPerfil(perfil);
                 mensagem = "Perfil adicionado com sucesso!";
            }

            if(ativo!=null){
                usuario.setAtivo(ativo);
                mensagem = ativo ? "Usuário ativado" : "Usuário desativado";

            }
            repository.save(usuario);

      return mensagem != null ? mensagem : "Nenhuma alteração realizada";
    }
    @Override
    @Transactional
    public String AlteraSenha(String email) {
        var response =  repository.findOneByEmail(email).orElseThrow(
                ()-> new RuntimeException("Usuario não existe!")
        );
        String novaPasswd = geraSenha();
       response.atualizaSenha(novaPasswd);
        repository.save(response);
     return novaPasswd;
    }

    @Override
    public UsuarioBuscaRequestDTO buscarUsuarioId(Long id) {
        UsuarioEntity usuario =repository.findById(id).orElseThrow(
                ()->new RuntimeException("não foi possivel encontrar usuario!")
        );
        return new UsuarioBuscaRequestDTO(usuario);
    }
    @Override
    public UsuarioRequestDTO buscarUsuario(Long id) {
        UsuarioEntity usuario =repository.findById(id).orElseThrow(
                ()->new RuntimeException("não foi possivel encontrar usuario!")
        );
        return new UsuarioRequestDTO(usuario);
    }

    @Override
    @Transactional
    public void salvaImagem(Long usuarioId, MultipartFile file) {
       var usuario = repository.findById(usuarioId).orElseThrow(
               ()->new RuntimeException("Não foi possivel localizar usuario!")
       );
        if (usuario.getAvatar() != null && !usuario.getAvatar().isBlank()) {
           var endpoint = endpointUrl+"/";
           var end = usuario.getAvatar().replace(endpoint,"");
           File imagemAntiga = new File(end);
           if (imagemAntiga.exists()) {
               System.out.println("deleletado "+end);
               imagemAntiga.delete();
           }
       }
     String nomeImagem = ValidaNomeImagem.criarDiretorio(file,"usuario",endpointUrl);
     repository.salvaImagem(nomeImagem,usuario.getId());

    }

    private String geraSenha(){
       String numeros = "0123456789";

       String maiuscula = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minucula = "abcdefghijklmnopqrstuvwxyz";
        String caracter = "!@#$%^&*=?";
        String todos = maiuscula+minucula+caracter+numeros;

        StringBuilder senha = new StringBuilder();
        var random = new Random();
        for(int i=0;i<8;i++){
            int index = random.nextInt(todos.length());
            senha.append(todos.charAt(index));
        }
        return senha.toString();

   }
   @Override
   @Transactional
   public void alteraSenha(AlteraSenhaDTO dto){
        var usuario = repository.findById(dto.usuarioId()).orElseThrow(
                ()->new RuntimeException("Não foi possivel localizar o usuario!")
        );
        validaNovaPassword.valid(dto,usuario.getPassword());
        usuario.atualizaSenha(dto.novaSenha());
        repository.salvaSenha(usuario.getPassword(),dto.usuarioId());
   }

    @Override
    public UsuarioRequestFiliasDTO filtraFiliais(Long id) {
        UsuarioEntity usuario =repository.findById(id).orElseThrow(
                ()->new RuntimeException("não foi possivel encontrar usuario!")
        );
        return new UsuarioRequestFiliasDTO(usuario);
    }


    private void salvaLog(UsuarioRequestDTO usuario, String acao,String descricao){
        String mensagem = String.format(
                "[%s] Usuário: %s | Ação: %s | Descrição: %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                usuario.nome(),
                acao,
                descricao
        );
        service.registrarLog(
                usuario,
                acao,
                mensagem
        );
    }

}
