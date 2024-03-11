package com.javacore.task.handlers;

import com.javacore.task.services.BruteForceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final BruteForceService bruteForceService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(@NotNull AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) request.getAttribute("LAST_USERNAME");
        bruteForceService.loginFailed(username);
        log.warn("Bad credentials for user {}", username);
    }
}
