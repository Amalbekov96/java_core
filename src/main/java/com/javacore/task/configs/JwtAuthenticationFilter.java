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
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        // Get the Authorization header from the request
        String authHeader = getAuthorizationHeader(request);

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            // Extract the token from the header
            String token = extractToken(authHeader);

            // Extract the username from the token
            String username = extractUsername(token);

            // Check if the username is not empty and the context authentication is null
            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load the user details
                UserDetails userDetails = loadUserByUsername(username);

                // Validate the token
                boolean isValid = isTokenValid(token, userDetails);

                if (isValid) {
                    // Set the authentication to the security context
                    setAuthenticationToContext(userDetails, request);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private String extractUsername(String token) {
        return jwtService.extractUserName(token);
    }

    private UserDetails loadUserByUsername(String username) {
        return userService.userDetailsService().loadUserByUsername(username);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        return jwtService.isTokenValid(token, userDetails);
    }

    private void setAuthenticationToContext(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }
}