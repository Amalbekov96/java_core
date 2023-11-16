package com.javacore.task.facades;


import com.javacore.task.services.TraineeService;
import com.javacore.task.services.TrainerService;
import com.javacore.task.services.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

}
