package com.javacore.workload.service.impl;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class AddSummaryAction implements SummaryAction {

    private final MonthlySummaryRepository monthlySummaryRepository;

    @Transactional
    @Override
    public void performAction(TrainerWorkloadRequest trainerWorkload) {
        String username = trainerWorkload.getUsername();

        String year = getYearAsString(trainerWorkload.getTrainingDate());
        String month = getMonthAsString(trainerWorkload.getTrainingDate());
        Months months = new Months(month, trainerWorkload.getTrainingDuration());
        Years newYear = new Years(year, List.of(months));

        Optional<MonthlySummary> optionalMonthlySummary = monthlySummaryRepository.findByUsername(username);

        if (optionalMonthlySummary.isPresent()) {
            MonthlySummary monthlySummary = optionalMonthlySummary.get();
            boolean yearExists = monthlySummary.getYears().stream()
                    .anyMatch(existingYear -> existingYear.getYear().equals(year) && existingYear.getMonths().stream()
                            .anyMatch(existingMonth -> existingMonth.getMonth().equals(month)));

            if (!yearExists) {
                monthlySummary.getYears().stream()
                        .filter(existingYear -> existingYear.getYear().equals(year))
                        .findFirst()
                        .ifPresentOrElse(
                                existingYear -> {
                                    existingYear.getMonths().add(months);
                                    log.info("action type ADD | adding trainer with name {} training with year {}", trainerWorkload.getUsername(), trainerWorkload.getTrainingDate().getYear());
                                },
                                () -> {
                                    monthlySummary.getYears().add(newYear);
                                    log.info("action type ADD | adding trainer with name {} training with year {}", trainerWorkload.getUsername(), trainerWorkload.getTrainingDate().getYear());
                                }
                        );
                AtomicInteger newDuration = new AtomicInteger(monthlySummary.getTrainingSummarizedDuration().intValue() + trainerWorkload.getTrainingDuration().intValue());
                monthlySummary.setTrainingSummarizedDuration(newDuration);
                monthlySummaryRepository.save(monthlySummary);
                log.info("action type ADD | adding trainer with name {} training with year {}", trainerWorkload.getUsername(), trainerWorkload.getTrainingDate().getYear());
            } else {
                log.info("action type ADD | trainer with name {} already exists", trainerWorkload.getUsername());
            }
        } else {
            MonthlySummary newSummary = createNewMonthlySummary(trainerWorkload);
            monthlySummaryRepository.save(newSummary);
            log.info("action type ADD | adding trainer with name {} training with year {}", trainerWorkload.getUsername(), trainerWorkload.getTrainingDate().getYear());
        }
    }
    private MonthlySummary createNewMonthlySummary(TrainerWorkloadRequest trainerWorkload) {
        List<Months> months = new ArrayList<>();
        months.add(new Months(getMonthAsString(trainerWorkload.getTrainingDate()), trainerWorkload.getTrainingDuration()));
        List<Years> years = new ArrayList<>();
        years.add(new Years(getYearAsString(trainerWorkload.getTrainingDate()), months));
        AtomicInteger defaultDuration = new AtomicInteger((Integer) trainerWorkload.getTrainingDuration());
        MonthlySummary newSummary = MonthlySummary.builder()
                .username(trainerWorkload.getUsername())
                .firstName(trainerWorkload.getFirstName())
                .lastName(trainerWorkload.getLastName())
                .isActive(trainerWorkload.isActive())
                .trainingSummarizedDuration(defaultDuration)
                .years(years)
                .build();
        log.info("creating new summary for trainer with name {}", trainerWorkload.getUsername());
        monthlySummaryRepository.save(newSummary);
        return newSummary;
    }
    private String getYearAsString(LocalDate trainingDate) {
        return String.valueOf(trainingDate.getYear());
    }

    private String getMonthAsString(LocalDate trainingDate) {
        return String.valueOf(trainingDate.getMonthValue());
    }

}
