package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

@SpringBootApplication
//@CrossOrigin(origins = "http://localhost:3000")
public class ApiGatewayServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ApiGatewayServiceApplication.class, args);
    }
    @Bean
    DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp){
        return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
    }
     public CorsWebFilter corsWebFilter(){
         CorsConfiguration corsConfig = new CorsConfiguration();
         corsConfig.applyPermitDefaultValues();
         corsConfig.addAllowedOrigin("http://localhost:3000"); // Replace with your allowed origin
         corsConfig.addAllowedMethod("DELETE");
         corsConfig.addAllowedMethod("POST");// Add specific methods if needed
         corsConfig.addAllowedMethod("PUT");// Add specific methods if needed
         corsConfig.addAllowedMethod("GET");// Add specific methods if needed

         return new CorsWebFilter(source -> new CorsConfiguration(corsConfig));

     }

}
