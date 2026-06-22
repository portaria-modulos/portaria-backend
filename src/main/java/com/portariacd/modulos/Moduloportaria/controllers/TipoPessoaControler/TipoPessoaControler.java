package com.portariacd.modulos.Moduloportaria.controllers.TipoPessoaControler;

import com.portariacd.modulos.Moduloportaria.TipoPessoa.TipoPessoaService;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaDTO;
import com.portariacd.modulos.Moduloportaria.domain.models.dto.tipoPessoa.TipoPessoaReuestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("portaria/tipo_pessoa")
public class TipoPessoaControler {
    private final TipoPessoaService service;
    public TipoPessoaControler(TipoPessoaService service){
        this.service = service;
    }
    @PostMapping("/registro")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_REGISTROS')")
    public ResponseEntity<Map<String,String>> registro(@RequestBody  @Valid  TipoPessoaDTO response) {
      try {
          service.registro(response);
          return ResponseEntity.ok().body(Map.of("msg","Item adicionado"));
      }catch (Exception e){
          throw  new RuntimeException(e.getMessage());
      }
    }
    @PutMapping("/atualiza")
    @PreAuthorize("@permissaoService.hasPermission(authentication, 'GERENCIAR_REGISTROS')")
    public ResponseEntity<Map<String,String>> atualiza(@RequestParam("id") long idTipoPessoa,@RequestBody @Valid TipoPessoaDTO response) {
        try {
            service.atualiza(idTipoPessoa,response);
            return ResponseEntity.ok().body(Map.of("msg","Item atualizado"));
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
     @DeleteMapping("/delete/{id}")
     @PreAuthorize("@permissaoService.hasPermission(authentication, 'DELETE_GLOBAL')")
     public ResponseEntity<Map<String,String>> delete(@PathVariable() long id) {
         try {
             service.delete(id);
             return ResponseEntity.ok().body(Map.of("msg","Item deletado"));
         }catch (Exception e){
             throw  new RuntimeException(e.getMessage());
         }
    }
     @GetMapping("/lista")
    public ResponseEntity<Map<String, List<TipoPessoaReuestDTO>>> lista() {
       var lista = service.lista();
        return  ResponseEntity.ok(lista);
    }
}
