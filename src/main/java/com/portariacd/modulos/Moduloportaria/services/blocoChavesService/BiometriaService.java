package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiometriaService {
    private final RestTemplate restTemplate;

    public BiometriaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${face}")
    private  String IA_URL;  // URL do seu container DeepFace
    @Autowired
    private CriptografiaService criptografiaService;
    public float[] extrairEmbedding(MultipartFile foto) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            ByteArrayResource resource = new ByteArrayResource(foto.getBytes()) {
                @Override
                public String getFilename() {
                    return foto.getOriginalFilename() != null
                            ? foto.getOriginalFilename()
                            : "foto.jpg";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    IA_URL + "processar",
                    request,
                    Map.class
            );

            Map bodyResponse = response.getBody();

            if (bodyResponse == null
                    || !bodyResponse.containsKey("embedding")
                    || bodyResponse.get("embedding") == null) {

                throw new RuntimeException(
                        "A IA não retornou o campo 'embedding'. Resposta recebida: "
                                + bodyResponse
                );
            }

            List<Double> embeddingList =
                    (List<Double>) bodyResponse.get("embedding");

            float[] embeddingArray = new float[embeddingList.size()];

            for (int i = 0; i < embeddingList.size(); i++) {
                embeddingArray[i] = embeddingList.get(i).floatValue();
            }

            return embeddingArray;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao processar biometria: " + e.getMessage(),
                    e
            );
        }
    }
    public float[] extrairEmbeddingFace(MultipartFile file) {
        try {
            // Otimize o ByteArrayResource reaproveitando o array de bytes diretamente
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename() != null ? file.getOriginalFilename() : "foto.jpg";
                }

                public org.springframework.http.MediaType getContentType() {
                    String contentType = file.getContentType();
                    return contentType != null
                            ? org.springframework.http.MediaType.parseMediaType(contentType)
                            : org.springframework.http.MediaType.IMAGE_JPEG;
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            // Usa a instância reaproveitada do RestTemplate
            ResponseEntity<Map> response = restTemplate.postForEntity(IA_URL + "reconhecer", request, Map.class);

            if (response.getBody() == null || !response.getBody().containsKey("embedding")) {
                throw new RuntimeException("A IA não retornou o embedding corretamente.");
            }

            List<Double> embeddingList = (List<Double>) response.getBody().get("embedding");

            float[] embeddingArray = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embeddingArray[i] = embeddingList.get(i).floatValue();
            }

            return embeddingArray;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar biometria: " + e.getMessage());
        }
    }
}