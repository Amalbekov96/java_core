package com.javacore.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/trainer/sign-up",
            "/api/auth/trainee/sign-up",
            "/api/auth/sign-in"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        log.info("Entering isSecured predicate");
        boolean isSecured = openApiEndpoints
                .stream()
                .anyMatch(uri -> {
                    boolean matches = request.getURI().getPath().contains(uri);
                    log.info("Request URI: {}, Open API Endpoint: {}, Matches: {}", request.getURI(), uri, matches);
                    return matches;
                });

        return !isSecured;
    };
}
