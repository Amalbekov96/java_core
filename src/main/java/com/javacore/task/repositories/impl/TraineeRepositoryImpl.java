package com.javacore.task.repositories.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.configs.InMemoryStorage;
import com.javacore.task.entities.Trainee;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.repositories.TraineeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TraineeRepositoryImpl implements TraineeRepository {

    private final InMemoryStorage inMemoryStorage;
    private final ObjectMapper objectMapper;

    @Override
    public Trainee findById(Long id) {
        Map<Long, Object> traineeStorage = inMemoryStorage.getEntityStorage("Trainee");
        if (traineeStorage != null) {
            return (Trainee) traineeStorage.get(id);
        }
        return null;
    }

    @Override
    public Trainee save(Trainee trainee) throws StorageException {

        if (trainee.getTraineeId() == null) {
            // Calculate the next ID
            Long nextId = inMemoryStorage.calculateNextId("Trainee");

            // Assign the calculated ID to the trainee entity
            trainee.setTraineeId(nextId);
            inMemoryStorage.addEntity("Trainee|" + nextId + "|" + serializeEntity(trainee));
        } else {
            // Update the existing record in the storage
            inMemoryStorage.updateEntity("Trainee|" + trainee.getTraineeId() + "|" + serializeEntity(trainee));
        }

        return trainee;
    }

    @Override
    public void deleteById(Long id) {
        Map<Long, Object> traineeStorage = inMemoryStorage.getEntityStorage("Trainee");
        if (traineeStorage != null) {
            traineeStorage.remove(id);
        }
    }

    @Override
    public List<Trainee> findAll() {
        Map<Long, Object> traineeStorage = inMemoryStorage.getEntityStorage("Trainee");
        if (traineeStorage != null) {
            return new ArrayList<>(traineeStorage.values().stream()
                    .map(entity -> (Trainee) entity)
                    .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    private String serializeEntity(Trainee trainee) throws StorageException {
        try {
            return objectMapper.writeValueAsString(trainee);
        } catch (JsonProcessingException e) {
            log.error("Error serializing Trainee entity", e);
            throw new StorageException("Error serializing Trainee entity");
        }
    }
}
