package com.portariacd.modulos.Moduloportaria.controllers;

import com.portariacd.modulos.Moduloportaria.services.UsuarioService;
import com.portariacd.modulos.Moduloportaria.domain.models.auth.AlteraSenhaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.CadastroUsuarioDto;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.usuarioVO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("portaria/v1/usuario")
public class UsuarioControler {
    private final UsuarioService service;
    public UsuarioControler(UsuarioService usuarioService){
        this.service = usuarioService;
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'CADASTRO_USUARIO')")
    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<?> cadastroUsuario(@RequestBody @Valid CadastroUsuarioDto dto){
            service.registroUsuario(dto);
            return ResponseEntity.ok().body(Map.of("msg","cadastrado com sucesso!"));
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    @GetMapping("/lista")
    public ResponseEntity<Page<UsuarioRequestDTO>> findAll(Pageable page, @RequestParam(name = "busca", required = false) String busca){
      var lista = service.listaUsuario(page,busca);
        return ResponseEntity.ok().body(lista);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<TokenResponse> login(@RequestBody UsuarioLoginDTO resquetaApi,
                                               HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        var dtod = new UsuarioLoginDTO(
                resquetaApi.email(),
                resquetaApi.password(),
                userAgent,
                getClientIp(request),
                userAgent
        ); var resposta = service.authLogin(dtod);
        return ResponseEntity.ok().body(resposta);
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Caso venha uma lista de IPs (em proxy)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    @GetMapping("/usuario-lista-perfil")
    public Map<String, UsuarioRequestPerfilDTO> buscaUsuarioPerfil(@RequestParam("email") String email) {
        return service.buscaUsuarioPerfil(email);
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'ADICIONAR_ACESSO')")
    @GetMapping("/usuario-add-perfil")
    public ResponseEntity AdicionarPerfil(@RequestParam("usuarioId") long usuarioId,
                                                              @RequestParam(value = "perfilId",required = false) Long perfilId,
                                                              @RequestParam(value = "ativo",required = false) Boolean ativo
                                                              ) {
        try {
          var msg =  service.adicionarPerfil(usuarioId,perfilId,ativo);
            return ResponseEntity.ok(Map.of("msg",msg));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_USUARIOS')")
    @PostMapping("/alterar/senha")
    @Transactional
    public ResponseEntity<?> AlteraSenha(@RequestParam("email") String email){
        String pwd = service.AlteraSenha(email);
        return ResponseEntity.ok(Map.of("psw",pwd));
    }
    @GetMapping("/busca/unit")
    public ResponseEntity<UsuarioBuscaRequestDTO> BuscaUsuarioUnico(@RequestParam("usuarioId") Long id){
        UsuarioBuscaRequestDTO usuario = service.buscaUsuarioId(id);
        return ResponseEntity.ok().body(usuario);
    }
    @GetMapping("/busca/valid")
    public ResponseEntity<UsuarioRequestDTO> BuscaUsuarioUnicoValid(@RequestParam("usuarioId") Long id){
        UsuarioRequestDTO usuario = service.buscaUsuario(id);
        return ResponseEntity.ok().body(usuario);
    }
    @GetMapping("/busca/filiais")
    public ResponseEntity<UsuarioRequestFiliasDTO> BuscaFiliaisUsuario(@RequestParam("usuarioId") Long id){
        UsuarioRequestFiliasDTO usuario = service.filtraFilial(id);
        return ResponseEntity.ok().body(usuario);
    }
    @PutMapping("/avatar")
    public ResponseEntity<?> adicionarImagem(@RequestParam("usuarioId") Long usuarioId, @RequestParam("file") MultipartFile file){
        try {
            service.salvaImagem(usuarioId,file);
            return ResponseEntity.ok().body(Map.of("msg","Imagem Adicionada"));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @PutMapping("/alterarSenha")
    public ResponseEntity<?> alteraSenha(@RequestBody @Valid AlteraSenhaDTO dto){
       try {
           service.alteraSenha(dto);
           return ResponseEntity.ok().body(Map.of("msg","Senha alterada com sucesso!"));
       }catch (RuntimeException e){
           throw new RuntimeException(e.getMessage());
       }
    }

}