package com.javacore.task.services;

import com.javacore.task.entities.Training;
import com.javacore.task.enums.WorkHourCalculateType;

import java.util.List;

public interface WorkHourCalculatorService {
    void create(Training training, WorkHourCalculateType workHourCalculateType);
    void create(List<Training> trainings, WorkHourCalculateType workHourCalculateType);
}
