package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.enums.UserRole;
import com.javacore.task.exceptions.BadCredentialsException;
import com.javacore.task.exceptions.BadRequestException;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingTypeRepository;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final BruteForceService bruteForceService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public SignUpResponse traineeSignUp(TraineeRequest request) {
        log.info("Trainee sign up request: {}", request);
        String username = profileService.generateUsername(request.getFirstName(), request.getLastName());
        log.info("Username generated: {}", username);
        String password = profileService.generateRandomPassword();
        log.info("Password generated: {}", password);
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .role(UserRole.TRAINEE)
                .password(encryptionService.encode(password))
                .isActive(true)
                .build();
        log.info("User created: {}", user);
        userRepository.save(user);

        Trainee trainee = Trainee.builder()
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .user(user)
                .build();
        log.info("Trainee created: {}", trainee);
        traineeRepository.save(trainee);

        log.info("Trainee signed up with id: {}", trainee.getTraineeId());

        String token = jwtService.generateToken(user);
        return SignUpResponse.builder()
                .token(token)
                .username(user.getUsername())
                .generatedPassword(password)
                .role(user.getRole())
                .build();
    }

    @Transactional
    @Override
    public SignUpResponse trainerSignUp(TrainerRequest request) {

        String username = profileService.generateUsername(request.getFirstName(), request.getLastName());
        String password = profileService.generateRandomPassword();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .role(UserRole.TRAINER)
                .isActive(true)
                .password(encryptionService.encode(password))
                .build();
        userRepository.save(user);

        TrainingType trainingType = trainingTypeRepository.findById(request.getSpecialization().getId()).orElseThrow(
                () -> {
                    log.warn("Response: Training type not found");
                    return new EntityNotFoundException("Training type not found");
                }
        );
        Trainer trainer = Trainer.builder()
                .user(user)
                .specialization(trainingType)
                .build();
        trainerRepository.save(trainer);

        log.info("Trainer signed up with id: {}", trainer.getId());

        String token = jwtService.generateToken(user);
        return SignUpResponse.builder()
                .token(token)
                .username(user.getUsername())
                .generatedPassword(password)
                .role(user.getRole())
                .build();
    }

    @Transactional
    @Override
    public void changePassword(String username, String password, String newPassword) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals(username)) {
            throw new BadCredentialsException("You can change only your password");
        }
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> {
            log.warn("Response: User not found");
            return new UserNotFoundException(String.format("User with username: %s not found", username));
        });
        if (!encryptionService.matches(password, user.getPassword())) {
            throw new com.javacore.task.exceptions.BadCredentialsException("wrong password");
        }
        userRepository.changePassword(user.getUserId(), encryptionService.encode(newPassword));
        log.info("Changed password for User");
    }


    @Override
    public SignInResponse signIn(SignInRequest request) {
        if (request.getUsername().isBlank() || request.getPassword().isBlank()) {
            throw new BadCredentialsException("Username or password is blank");
        }
        if (bruteForceService.isBlocked(request.getUsername())) {
            throw new BadRequestException("User is blocked for 5 min");
        }
        User user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(
                () -> {
                    log.warn("Response: User not found");
                    return new EntityNotFoundException("User not found");
                }
        );

        if (!encryptionService.matches(request.getPassword(), user.getPassword())) {
            log.warn("Response: Wrong password");
            publishBadCredentialsEvent(request.getUsername(), (request.getPassword()));
            throw new BadCredentialsException("wrong credentials");
        }

        log.info("User signed in with username: {}", user.getUsername());
        String jwtToken = jwtService.generateToken(user);
        publishSuccessEvent(request.getUsername(), request.getPassword());
        return SignInResponse.builder().
                token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }


    private void publishBadCredentialsEvent(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        AuthenticationException exception = new AuthenticationException("Bad credentials") {
        };
        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(authentication, exception);
        eventPublisher.publishEvent(event);
    }

    private void publishSuccessEvent(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);
        eventPublisher.publishEvent(event);
    }
}