package com.javacore.task.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String LAST_USERNAME_KEY = "LAST_USERNAME";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        request.getSession().setAttribute(LAST_USERNAME_KEY, request.getParameter("username"));
        super.onAuthenticationFailure(request, response, exception);
    }
}
