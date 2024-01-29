package com.javacore.task.services.impl;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.entities.User;
import com.javacore.task.enums.UserRole;
import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.repositories.TrainingTypeRepository;
import com.javacore.task.repositories.UserRepository;
import com.javacore.task.services.AuthenticationService;
import com.javacore.task.services.JwtService;
import com.javacore.task.services.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfileService profileService;
    @Override
    public SignUpResponse traineeSignUp(TraineeRequest request) {
        log.info("Trainee sign up request: {}", request);
        String username = profileService.generateUsername(request.firstName(), request.lastName());
        log.info("Username generated: {}", username);
        String password = profileService.generateRandomPassword();
        log.info("Password generated: {}", password);
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(username)
                .role(UserRole.TRAINEE)
                .password(passwordEncoder.encode(password))
                .isActive(true)
                .build();
        log.info("User created: {}", user);
        userRepository.save(user);

        Trainee trainee = Trainee.builder()
                .address(request.address())
                .dateOfBirth(request.dateOfBirth())
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

    @Override
    public SignUpResponse trainerSignUp(TrainerRequest request) {

        String username = profileService.generateUsername(request.firstName(), request.lastName());
        String password = profileService.generateRandomPassword();
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(username)
                .role(UserRole.TRAINER)
                .isActive(true)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);

        TrainingType trainingType = trainingTypeRepository.findById(request.specialization().getId()).orElseThrow(
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


    @Override
    public SignInResponse signIn(SignInRequest request) {

        if (request.username().isBlank() || request.password().isBlank()) {
            throw new BadCredentialsException("Username or password is blank");
        }
        User user = userRepository.findUserByUsername(request.username()).orElseThrow(
                () -> {
                    log.warn("Response: User not found");
                    return new EntityNotFoundException("User not found");
                }
        );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Response: Wrong password");
                throw new BadCredentialsException("wrong credentials");
            }

     log.info("User signed in with username: {}", user.getUsername());
        String jwtToken = jwtService.generateToken(user);
        return SignInResponse.builder().
                token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}