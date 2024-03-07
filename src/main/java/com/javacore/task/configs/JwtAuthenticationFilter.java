package com.javacore.task.configs;

import com.javacore.task.services.JwtService;
import com.javacore.task.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            putUserToContext(request);
        } catch (Exception ex) {
            log.warn("Unknown server error e {}", ex.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
//        final String jwt = authHeader.substring(BEARER.length());
//        final String username = jwtService.extractUserName(jwt);
//        if (StringUtils.isNotEmpty(username)
//                && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userService.userDetailsService()
//                    .loadUserByUsername(username);
//            if (jwtService.isTokenValid(jwt, userDetails)) {
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                context.setAuthentication(authToken);
//                SecurityContextHolder.setContext(context);
//            }
//        }
//        filterChain.doFilter(request, response);
    }

    private void putUserToContext(HttpServletRequest request) {
        Optional.ofNullable(getAuthorizationHeader(request))
                .map(this::extractToken)
                .filter(this::isTokenValid)
                .map(this::retrieveUserDetails)
                .ifPresent(this::setAuthenticationToContext);
    }

    private boolean isTokenValid(String token) {
        return jwtService.validateToken(token);
    }

    private void setAuthenticationToContext(UserDetails userDetails) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);
    }

    private UserDetails retrieveUserDetails(String token) {
        return userService.userDetailsService().loadUserByUsername(extractUsername(token));
    }

    private String extractUsername(String token) {
        return jwtService.extractUserName(token);
    }

    private String extractToken(String header) {
        return header.substring(BEARER.length());
    }

    @Nullable
    private String getAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}