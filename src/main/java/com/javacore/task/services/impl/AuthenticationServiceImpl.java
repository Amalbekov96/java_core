package com.javacore.task.services.impl;

import com.javacore.task.entities.User;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.JwtAuthenticationResponse;
import com.javacore.task.models.SignUpRequest;
import com.javacore.task.models.SigninRequest;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.AuthenticationService;
import com.javacore.task.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .username(request.getUsername()).role(request.getRole()).password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}