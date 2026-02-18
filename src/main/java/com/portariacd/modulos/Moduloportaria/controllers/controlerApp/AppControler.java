package com.portariacd.modulos.Moduloportaria.controllers.controlerApp;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@RestController
@RequestMapping
// É boa prática ter um prefixo
public class AppControler {

    private final ConcurrentHashMap<String, String> tokenApp = new ConcurrentHashMap<>();
    private final String versionApp = "1.0.1";
    @GetMapping("/download-app/{token}")
    public ResponseEntity<Resource> downloadsApp(@PathVariable String token) {
        String fileName = tokenApp.get(token);

        if (fileName == null) {
            return ResponseEntity.status(403).build();
        }
        try {

            var resourceUrl = getClass()
                    .getClassLoader()
                    .getResource("static/portariav1.apk");

            if (resourceUrl == null) {
                return ResponseEntity.notFound().build();
            }

            File file = new File(resourceUrl.toURI());

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            tokenApp.remove(token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.android.package-archive"))
                    .contentLength(file.length()) //  envia o tamanho do arquivo para o download
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/app-downloads")
    public ResponseEntity<AppDTO> downloadConfig() {
        String token = UUID.randomUUID().toString();
        tokenApp.put(token, "app-portaria.apk");

        // Dica: Tente não deixar a URL do Ngrok hardcoded, mas para teste funciona
        String urlApp = "http://192.168.88.220:8083/download-app/" + token;

        var resposta = new AppDTO("App-monitoramento", versionApp, urlApp,"Melhoria no modulo recebimento");
        return ResponseEntity.ok(resposta);
    }
}