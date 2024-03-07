package com.javacore.task.handlers;

import com.javacore.task.services.BruteForceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final BruteForceService bruteForceService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = request.getParameter("username");
        bruteForceService.resetCache(username);
    }
}
