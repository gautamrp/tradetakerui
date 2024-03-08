package com.tradetaker.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class CorsConfiguration implements WebMvcConfigurer {

    @Value('http://localhost:3000')
    private String[] corsUrl


    @Override
    void addCorsMappings(CorsRegistry registry) {
        registry.addMapping('/**')
                .allowedMethods('GET', 'POST', 'PUT', 'DELETE').allowedOrigins(corsUrl)
    }
}
