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
        // If the Trainer has an ID, it means it already exists, so update it
        if (trainer.getId() != null) {
            inMemoryStorage.updateEntity("Trainer|" + trainer.getId() + "|" + serializeEntity(trainer));
        } else {
            // If there's no ID, calculate the next ID and save the new Trainer entity
            Long nextId = inMemoryStorage.calculateNextId("Trainer");
            trainer.setId(nextId);
            inMemoryStorage.addEntity("Trainer|" + nextId + "|" + serializeEntity(trainer));
        }

        // Return the saved or updated entity
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
