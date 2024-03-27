package com.javacore.task.services;

import com.javacore.task.models.request.SignInRequest;
import com.javacore.task.models.request.TraineeRequest;
import com.javacore.task.models.request.TrainerRequest;
import com.javacore.task.models.response.SignInResponse;
import com.javacore.task.models.response.SignUpResponse;

public interface AuthenticationService {
    SignUpResponse traineeSignUp(TraineeRequest request);
    SignUpResponse trainerSignUp(TrainerRequest request);
    SignInResponse signIn(SignInRequest request);
    void changePassword(String username, String password, String newPassword);
}
