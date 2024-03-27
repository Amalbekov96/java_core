package com.javacore.workload.service.impl;

import com.javacore.workload.exception.EntityNotFoundException;
import com.javacore.workload.exception.IllegalArgumentException;
import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.model.Months;
import com.javacore.workload.model.Years;
import com.javacore.workload.DTO.request.TrainerWorkloadRequest;
import com.javacore.workload.repo.MonthlySummaryRepository;
import com.javacore.workload.service.SummaryAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DeleteSummaryAction implements SummaryAction {

    private final MonthlySummaryRepository monthlySummaryRepository;

    @Transactional
    @Override
    public void performAction(TrainerWorkloadRequest trainerWorkload) {
        String year = getYearAsString(trainerWorkload.getTrainingDate());
        String month = getMonthAsString(trainerWorkload.getTrainingDate());
        Months months = new Months(month, trainerWorkload.getTrainingDuration());
        Years years = new Years(year, List.of(months));

        MonthlySummary monthlySummary = monthlySummaryRepository
                .findByUsername(trainerWorkload.getUsername())
                .orElseThrow(() -> {
                    log.warn("No such summary with username {}", trainerWorkload.getUsername());
                    return new EntityNotFoundException("No such summary with username " + trainerWorkload.getUsername());
                });

        if (monthlySummary.getYears().contains(years)) {
            monthlySummary.getYears().remove(years);
            log.info("action type DELETE | deleting trainer with name {} training with year {}",trainerWorkload.getUsername(),trainerWorkload.getTrainingDate().getYear());
            monthlySummaryRepository.save(monthlySummary);
        }else {
            log.warn("No such year in summary");
            throw new IllegalArgumentException(String.format("No such year in summary with username %s and year %d and month %d and duration %d",
                    trainerWorkload.getUsername(),
                    trainerWorkload.getTrainingDate().getYear(),
                    trainerWorkload.getTrainingDate().getMonthValue(),
                    (int) trainerWorkload.getTrainingDuration()));

        }
    }
    private String getYearAsString(LocalDate trainingDate) {
        return String.valueOf(trainingDate.getYear());
    }

    private String getMonthAsString(LocalDate trainingDate) {
        return String.valueOf(trainingDate.getMonthValue());
    }
}
