package com.mehmetsadullahguven.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // tüm endpointler için geçerli
                .allowedOrigins("http://localhost:3039", "http://localhost:5173") // React frontend adresin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // izin verilen metodlar
                .allowedHeaders("*"); // tüm header'lara izin ver
    }
}
