package com.hackathon.fiap.mscliente.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class GetAuth {

    public static String getToken() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, String> request = new HashMap<>();
        request.put("usuario", "adj2");
        request.put("senha", "adj@1234");

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081" + "/api/autenticacao",
                HttpMethod.POST,
                entity,
                String.class
        );
        return getBodyTokenResponse(response.getBody());
    }

    private static String getBodyTokenResponse(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(body);
        return jsonNode.path("token").asText();
    }
}
