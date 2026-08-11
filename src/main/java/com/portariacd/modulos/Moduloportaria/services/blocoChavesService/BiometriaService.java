package com.portariacd.modulos.Moduloportaria.services.blocoChavesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class BiometriaService {
    private final String IA_URL = "http://10.220.112.111:5000/"; // URL do seu container DeepFace
    @Autowired
    private CriptografiaService criptografiaService;
    public float[] extrairEmbedding(String fotoBase64) {
        System.out.println("base " + fotoBase64);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = new HashMap<>();
        request.put("base64", fotoBase64);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(IA_URL + "processar", request, Map.class);

            Map body = response.getBody();
            if (body == null || !body.containsKey("embedding") || body.get("embedding") == null) {
                throw new RuntimeException("A IA não retornou o campo 'embedding'. Resposta recebida: " + body);
            }

            List<Double> embeddingList = (List<Double>) body.get("embedding");
            float[] embeddingArray = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                embeddingArray[i] = embeddingList.get(i).floatValue();
            }
            return embeddingArray;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar biometria: " + e.getMessage());
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