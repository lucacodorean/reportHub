package com.reporthub.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.api_url}")     private  String apiUrl;
    @Value("${app.api_version}") private  String apiVersion;
    @Value("${app.api_port}")    private  String apiPort;

    private static String API_URL;
    private static String API_VERSION;
    private static String API_PORT;

    @PostConstruct
    public void init() {
        API_URL = apiUrl;
        API_VERSION = apiVersion;
        API_PORT = apiPort;
    }

    public static String getAPILink() {
        StringBuilder url = new StringBuilder(API_URL + ":" + API_PORT + "/api/" + API_VERSION);
        return url.toString();
    }
}
