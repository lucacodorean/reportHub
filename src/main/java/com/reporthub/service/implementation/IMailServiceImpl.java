package com.reporthub.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reporthub.config.AppConfig;
import com.reporthub.service.IMailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class IMailServiceImpl implements IMailService {


    private final String emailServiceUrl = AppConfig.getEmailServiceUrl();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper objectMapper;

    public IMailServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Async
    public void sendMail(String to, String subject, String body) {
        System.out.println(emailServiceUrl);

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("to", to);
            payload.put("subject", subject);
            payload.put("body", body);

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(emailServiceUrl))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 == 2) {
                System.out.println("Email microservice responded: " + response.body());
            } else {
                System.err.println("Email microservice error: " + response.statusCode()
                        + " - " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Failed to call email microservice: " + e.getMessage());
        }
    }
}
