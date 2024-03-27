package com.javacore.task.clients;

import com.javacore.task.exceptions.UnexpectedException;
import com.javacore.task.models.CreateWorkHourCalculatorModel;
import com.javacore.task.models.WorkHourCalculatorModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkHourCalculatorServiceFallback implements WorkHourCalculatorClient {
    private static final String ERROR_MSG = "Workload service is unavailable";

    @Override
    public WorkHourCalculatorModel create(CreateWorkHourCalculatorModel createWorkHourCalculatorModel) {
        throw new UnexpectedException(ERROR_MSG);
    }

    @Override
    public List<WorkHourCalculatorModel> create(List<CreateWorkHourCalculatorModel> createWorkHourCalculatorModels) {
        throw new UnexpectedException(ERROR_MSG);
    }
}
