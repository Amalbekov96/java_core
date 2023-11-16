package com.javacore.task.repositories.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.configs.InMemoryStorage;
import com.javacore.task.entities.Trainer;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.repositories.TrainerRepository;
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
public class TrainerRepositoryImpl implements TrainerRepository {

    private final InMemoryStorage inMemoryStorage;
    private final ObjectMapper objectMapper;

    @Override
    public Trainer findById(Long id) {
        Map<Long, Object> trainerStorage = inMemoryStorage.getEntityStorage("Trainer");
        if (trainerStorage != null) {
            return (Trainer) trainerStorage.get(id);
        }
        return null;
    }

    @Override
    public Trainer save(Trainer trainer) throws StorageException {
        // Calculate the next ID
        Long nextId = inMemoryStorage.calculateNextId("Trainer");

        // Assign the calculated ID to the trainer entity
        trainer.setId(nextId);

        // Add the entity to the storage
        inMemoryStorage.addEntity("Trainer|" + nextId + "|" + serializeEntity(trainer));

        // Return the saved entity
        return trainer;
    }

    @Override
    public void deleteById(Long id) {
        Map<Long, Object> trainerStorage = inMemoryStorage.getEntityStorage("Trainer");
        if (trainerStorage != null) {
            trainerStorage.remove(id);
        }
    }

    @Override
    public List<Trainer> findAll() {
        Map<Long, Object> trainerStorage = inMemoryStorage.getEntityStorage("Trainer");
        if (trainerStorage != null) {
            return new ArrayList<>(trainerStorage.values().stream()
                    .map(entity -> (Trainer) entity)
                    .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    private String serializeEntity(Trainer trainer) throws StorageException {
        try {
            return objectMapper.writeValueAsString(trainer);
        } catch (JsonProcessingException e) {
            log.error("Error serializing Trainer entity", e);
            throw new StorageException("Error serializing Trainer entity");
        }
    }
}
