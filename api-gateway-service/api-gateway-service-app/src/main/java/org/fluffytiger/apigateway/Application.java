package org.fluffytiger.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("connectors-registry-service-route", r -> r.path("/connectors/**")
                .uri("http://connectors-registry-service:8080"))
            .route("wage-service-route", r -> r.path("/wages/**")
                .uri("http://wage-service:8080"))
            .build();
    }

}
