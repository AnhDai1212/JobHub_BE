package com.daita.datn.services.implement;

import com.daita.datn.services.EmbeddingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmbeddingServiceImpl implements EmbeddingService {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingServiceImpl.class);

    ObjectMapper objectMapper;

    @NonFinal
    @Value("${embedding.api.url:}")
    String apiUrl;

    @NonFinal
    @Value("${embedding.api.key:}")
    String apiKey;

    @NonFinal
    @Value("${embedding.api.model:}")
    String model;

    @Override
    public List<Double> embedText(String text) {
        if (text == null || text.isBlank()) {
            logger.debug("Embedding skipped: empty text");
            return null;
        }
        if (apiUrl == null || apiUrl.isBlank() || model == null || model.isBlank()) {
            logger.debug("Embedding skipped: missing apiUrl/model");
            return null;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("input", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        Map<?, ?> response;
        try {
            logger.info("Embedding request -> {} (model={})", apiUrl, model);
            response = restTemplate.postForObject(apiUrl, request, Map.class);
        } catch (Exception e) {
            logger.warn("Embedding request failed: {}", e.getMessage());
            return null;
        }

        if (response == null) {
            return null;
        }

        Object data = response.get("data");
        if (!(data instanceof List<?> dataList) || dataList.isEmpty()) {
            logger.warn("Embedding response missing data array");
            return null;
        }

        Object first = dataList.get(0);
        if (!(first instanceof Map<?, ?> firstMap)) {
            return null;
        }

        Object embedding = firstMap.get("embedding");
        if (embedding == null) {
            logger.warn("Embedding response missing embedding field");
            return null;
        }

        try {
            List<Double> vector = objectMapper.convertValue(embedding, new TypeReference<List<Double>>() {});
            logger.info("Embedding success (dims={})", vector != null ? vector.size() : 0);
            return vector;
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to parse embedding: {}", e.getMessage());
            return null;
        }
    }
}
