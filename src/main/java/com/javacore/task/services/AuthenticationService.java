package com.javacore.task.services;

import com.javacore.task.models.JwtAuthenticationResponse;
import com.javacore.task.models.SignUpRequest;
import com.javacore.task.models.SigninRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
