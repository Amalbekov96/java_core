package com.javacore.workload.service;

import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.DTO.request.TrainerWorkloadRequest;

import java.util.List;

public interface SummaryService {
    void calculateSummary(TrainerWorkloadRequest trainerWorkload);

    MonthlySummary getSummaryById(String id);

    void deleteSummaryByUsername(String username);

    MonthlySummary getSummaryByUsername(String username);

    List<MonthlySummary> getAllSummaries();

    void updateSummary(String id, TrainerWorkloadRequest request);

}