package com.reporthub.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AppConfig implements WebMvcConfigurer {

    @Value("${app.api_url}")     private  String apiUrl;
    @Value("${app.api_version}") private  String apiVersion;
    @Value("${app.api_port}")    private  String apiPort;
    @Value("${spring.servlet.multipart.location}") private String attachmentLocation;
    @Value("${app.email_address}")   private  String emailAddress;

    private static String API_URL;
    private static String API_VERSION;
    private static String API_PORT;
    private static String ATTACHMENT_PATH;
    private static String EMAIL_ADDRESS;

    @PostConstruct
    public void init() {
        API_URL = apiUrl;
        API_VERSION = apiVersion;
        API_PORT = apiPort;
        ATTACHMENT_PATH = attachmentLocation;
        EMAIL_ADDRESS = emailAddress;
    }

    public static String getAPILink() {
        return API_URL + ":" + API_PORT + "/api/" + API_VERSION;
    }

    public static String getAttachmentPath() { return ATTACHMENT_PATH; }

    public static String getAPIUrl() { return API_URL + ":" + API_PORT; }

    public static String getEmailAddress() { return EMAIL_ADDRESS; }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/attachments/**")
                .addResourceLocations("file:./uploads/attachments/");
    }
}
