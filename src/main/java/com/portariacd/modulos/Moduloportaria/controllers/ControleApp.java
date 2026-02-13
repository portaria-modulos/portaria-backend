package com.portariacd.modulos.Moduloportaria.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/download")
public class ControleApp {
    @GetMapping("/portaria")
    public ResponseEntity<?> downloadApp() throws JsonProcessingException {
        var itens = """
{
  "version": 1,
  "name": "app.apk",
  "upload": true
}
""";

        var js = new ObjectMapper();
        var jss = js.readValue(itens, ControleApp.js.class);
        return ResponseEntity.ok(jss);
    }

    public  record js(Integer version,String name,boolean upload){}

}


