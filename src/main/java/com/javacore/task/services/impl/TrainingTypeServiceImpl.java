package com.javacore.task.services.impl;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.repositories.TrainingTypeRepository;
import com.javacore.task.services.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeDao;
    @Override
    public List<TrainingType> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();
        log.info("Retrieved all Training Types: {}", trainingTypes);
        return trainingTypes;
    }
}
