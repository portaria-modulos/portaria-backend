package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final String IA_URL = "http://10.220.112.111:5000/"; // URL do seu container DeepFace
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
    public float[] extrairEmbeddingFace(String fotoBase64) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = new HashMap<>();
        request.put("base64", fotoBase64);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(IA_URL+"reconhecer", request, Map.class);

            // O RestTemplate pode retornar uma lista de Double.
            // Precisamos converter para float[] primitivo para o pgvector
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