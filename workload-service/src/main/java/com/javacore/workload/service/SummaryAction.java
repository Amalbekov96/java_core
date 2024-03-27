package com.javacore.workload.service;

import com.javacore.workload.DTO.request.TrainerWorkloadRequest;

public interface SummaryAction {
    void performAction(TrainerWorkloadRequest trainerWorkload);

}
