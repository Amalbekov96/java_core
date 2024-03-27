package com.javacore.workload.service.impl;

import com.javacore.workload.exception.EntityNotFoundException;
import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.model.Months;
import com.javacore.workload.model.Years;
import com.javacore.workload.model.enums.Operation;
import com.javacore.workload.DTO.request.TrainerWorkloadRequest;
import com.javacore.workload.repo.MonthlySummaryQueryDao;
import com.javacore.workload.repo.MonthlySummaryRepository;
import com.javacore.workload.service.SummaryAction;
import com.javacore.workload.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@Transactional
public class SummaryServiceImpl implements SummaryService {

    private final MonthlySummaryRepository monthlySummaryRepository;
    private final MonthlySummaryQueryDao monthlySummaryQueryDao;

    private final Map<String, SummaryAction> actionStrategies = new HashMap<>();

    @Autowired
    public SummaryServiceImpl(MonthlySummaryRepository monthlySummaryRepository, MonthlySummaryQueryDao monthlySummaryQueryDao) {
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.monthlySummaryQueryDao = monthlySummaryQueryDao;
        actionStrategies.put(Operation.ADD.toString(), new AddSummaryAction(monthlySummaryRepository));
        actionStrategies.put(Operation.DELETE.toString(), new DeleteSummaryAction(monthlySummaryRepository));
    }

    public void calculateSummary(TrainerWorkloadRequest trainerWorkload) {

        String actionType = trainerWorkload.getActionType();
        SummaryAction actionStrategy = actionStrategies.get(actionType);

        if (actionStrategy != null) {
            actionStrategy.performAction(trainerWorkload);
        } else {
            log.warn("No such action type: {}", actionType);
            throw new java.lang.IllegalArgumentException("No such action type: " + actionType);
        }
    }

    @Override
    public MonthlySummary getSummaryById(String id) {
        return monthlySummaryRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("No such summary with id {}", id);
                    return new EntityNotFoundException("No such summary with id " + id);
                }
        );
    }

    @Override
    public void deleteSummaryByUsername(String username) {
        log.info("deleteSummaryByUsername method is called with username {}", username);
        monthlySummaryQueryDao.deleteSummaryByUsername(username);
    }

    @Override
    public MonthlySummary getSummaryByUsername(String username){
            log.info("getSummary method is called with username {}", username);
            return monthlySummaryRepository.findByUsername(username).orElseThrow(
                    () -> {
                        log.warn("No such summary with username {}", username);
                        return new EntityNotFoundException("No such summary with username " + username);
                    }
            );
        }

    @Transactional
    @Override
    public void updateSummary(String id, TrainerWorkloadRequest trainerWorkload) {

        String year = getYearAsString(trainerWorkload.getTrainingDate());
        String month = getMonthAsString(trainerWorkload.getTrainingDate());
        Months months = new Months(month, trainerWorkload.getTrainingDuration());
        Years years = new Years(year, List.of(months));

        MonthlySummary monthlySummary = getSummaryById(id);
        monthlySummary.setUsername(trainerWorkload.getUsername());
        monthlySummary.setFirstName(trainerWorkload.getFirstName());
        monthlySummary.setLastName(trainerWorkload.getLastName());
        monthlySummary.setActive(trainerWorkload.isActive());
        monthlySummary.getYears().add(years);
        log.info("updating summary for trainer with name {}", trainerWorkload.getUsername());
        monthlySummaryRepository.save(monthlySummary);
    }

    @Override
    public List<MonthlySummary> getAllSummaries() {
        log.info("getAllSummaries method is called");
            return monthlySummaryRepository.findAll();
        }

    private String getYearAsString(LocalDate trainingDate) {
        log.info("getYearAsString method is called");
        return String.valueOf(trainingDate.getYear());
    }

    private String getMonthAsString(LocalDate trainingDate) {
        log.info("getMonthAsString method is called");
        return String.valueOf(trainingDate.getMonthValue());
    }

}

