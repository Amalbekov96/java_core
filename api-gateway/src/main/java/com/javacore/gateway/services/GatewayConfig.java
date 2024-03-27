package com.javacore.gateway.services;

import com.javacore.gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("trainer-workload", r -> r.path("/trainer-workload/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://trainer-workload"))
                .route("main-service", r -> r.path("/api/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://main-service"))
                .build();
    }
}
