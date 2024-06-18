package com.example.dentistappointment.WebConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Dodajte vaš frontend URL ovde
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Dodajte dozvoljene HTTP metode
                .allowedHeaders("*") // Dodajte dozvoljene zaglavlja
                .allowCredentials(true); // Omogućite kredencijale
    }
}
