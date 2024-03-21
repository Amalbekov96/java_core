package com.javacore.task.services.impl;


import com.javacore.task.clients.WorkHourCalculatorClient;
import com.javacore.task.entities.Training;
import com.javacore.task.enums.WorkHourCalculateType;
import com.javacore.task.models.CreateWorkHourCalculatorModel;
import com.javacore.task.services.WorkHourCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkHourCalculatorServiceImpl implements WorkHourCalculatorService {
    private final WorkHourCalculatorClient workHourCalculatorClient;

    @Override
    public void create(Training training, WorkHourCalculateType workHourCalculateType) {
        CreateWorkHourCalculatorModel createWorkloadModel = toCreateWorkloadModel(training, workHourCalculateType);
        workHourCalculatorClient.create(createWorkloadModel);
        log.debug("Created a workload with a model {}", createWorkloadModel);
    }

    @Override
    public void create(List<Training> trainings, WorkHourCalculateType workHourCalculateType) {
        List<CreateWorkHourCalculatorModel> createWorkHourCalculatorModels = new ArrayList<>();

        for (Training training : trainings) {
            createWorkHourCalculatorModels.add(toCreateWorkloadModel(training, workHourCalculateType));
        }

        workHourCalculatorClient.create(createWorkHourCalculatorModels);
        log.debug("Created workloads with models {}", createWorkHourCalculatorModels);
    }

    private CreateWorkHourCalculatorModel toCreateWorkloadModel(Training training, WorkHourCalculateType workHourCalculateType) {
        return CreateWorkHourCalculatorModel.builder()
                .trainerUsername(training.getTrainer().getUsername())
                .trainerFirstName(training.getTrainer().getFirstName())
                .trainerLastName(training.getTrainer().getLastName())
                .isActive(training.getTrainer().getActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(workHourCalculateType)
                .build();
    }
}
