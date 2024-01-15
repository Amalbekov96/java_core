package com.javacore.task.repositories.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.repositories.InMemoryStorage;
import com.javacore.task.entities.Training;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.repositories.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final InMemoryStorage inMemoryStorage;
    private final ObjectMapper objectMapper;

    @Override
    public Training findById(Long id) {
        Map<Long, Object> trainingStorage = inMemoryStorage.getEntityStorage("Training");
        if (trainingStorage != null) {
            return (Training) trainingStorage.get(id);
        }
        return null;
    }

    @Override
    public Training save(Training training) throws StorageException {
        // Calculate the next ID
        Long nextId = inMemoryStorage.calculateNextId("Training");

        // Assign the calculated ID to the training entity
        training.setId(nextId);

        // Add the entity to the storage
        inMemoryStorage.addEntity("Training|" + nextId + "|" + serializeEntity(training));

        // Return the saved entity
        return training;
    }

    @Override
    public void deleteById(Long id) {
        Map<Long, Object> trainingStorage = inMemoryStorage.getEntityStorage("Training");
        if (trainingStorage != null) {
            trainingStorage.remove(id);
        }
    }

    @Override
    public List<Training> findAll() {
        Map<Long, Object> trainingStorage = inMemoryStorage.getEntityStorage("Training");
        if (trainingStorage != null) {
            return new ArrayList<>(trainingStorage.values().stream()
                    .map(entity -> (Training) entity)
                    .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    private String serializeEntity(Training training) throws StorageException {
        try {
            return objectMapper.writeValueAsString(training);
        } catch (JsonProcessingException e) {
            log.error("Error serializing Training entity", e);
            throw new StorageException("Error serializing Training entity");
        }
    }
}
