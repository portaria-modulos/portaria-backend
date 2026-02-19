package com.portariacd.modulos.Moduloportaria.controllers.controlerApp;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@RestController
@RequestMapping
public class AppControler {
    private final ConcurrentHashMap<String, String> tokenApp = new ConcurrentHashMap<>();
    private final String versionApp = "1.0.2";
    @Value("${app.apk.path}")
    private String apkPath;
    @GetMapping("/download-app/{token}")
    public ResponseEntity<Resource> downloadsApp(@PathVariable String token) {
        System.out.println("chegou");
        String fileName = tokenApp.get(token);
        System.out.println("data "+fileName);

        if (fileName == null) {
            return ResponseEntity.status(403).build();
        }

        try {

            File file = new File(apkPath);
            if(!file.exists()){
                file.mkdirs();
            }

            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            tokenApp.remove(token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.android.package-archive"))
                    .contentLength(file.length())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/app-downloads")
    public ResponseEntity<AppDTO> downloadConfig() {
        String token = UUID.randomUUID().toString();
        tokenApp.put(token, "portariav1.apk");

        // Dica: Tente não deixar a URL do Ngrok hardcoded, mas para teste funciona
        String urlApp = "http://192.168.88.220:8083/download-app/" + token;

        var resposta = new AppDTO("App-monitoramento", versionApp, urlApp,"Melhoria no modulo recebimento");
        return ResponseEntity.ok(resposta);
    }
    @PostConstruct
    public void criarPastaSeNaoExistir() {
        File pasta = new File("app");

        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }
}