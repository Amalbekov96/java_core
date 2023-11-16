package com.javacore.task.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.exceptions.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryStorage {

    private final ObjectMapper objectMapper;

    private final Map<String, Map<Long, Object>> entityStorage = new HashMap<>();

    @Value("${data.file.path}")
    private String dataFilePath;

    @PostConstruct
    public void initialize() throws IOException {
        // Initialize storage with prepared data from the file
        if (dataFilePath != null && !dataFilePath.isEmpty()) {
            List<String> lines = Files.readAllLines(Path.of(dataFilePath));
            // Assuming each line in the file corresponds to a serialized entity
            lines.forEach(this::addEntity);
        }

        System.out.println(entityStorage);
    }

    public void addEntity(String entityData) {
        // Assuming entity data is in the format "namespace,id,serializedEntity"
        String[] parts = entityData.split("\\|");
        try {
            if (parts.length == 3) {
                String entityNamespace = parts[0];
                Long id = Long.parseLong(parts[1]);
                // Deserialize the entity based on the actual structure
                Object entity = deserializeEntity(entityNamespace,parts[2]);
                entityStorage.computeIfAbsent(entityNamespace, k -> new HashMap<>()).put(id, entity);
            }
        } catch (StorageException | JsonProcessingException e) {
            log.error("Could not parse json {} with user tpye {}", parts[2], parts[0]);
        }
    }

    private Object deserializeEntity(String entityNamespace, String serializedEntity) throws StorageException, JsonProcessingException {
        switch (entityNamespace){
            case "Trainer":
                return objectMapper.readValue(serializedEntity, Trainer.class);
            case "Trainee":
                return objectMapper.readValue(serializedEntity, Trainee.class);
            case "Training":
                return objectMapper.readValue(serializedEntity, Training.class);
            default:
                throw new StorageException("There is no type " + entityNamespace);
        }
    }

    public Map<Long, Object> getEntityStorageByNamespace(String namespace) {
        return entityStorage.getOrDefault(namespace, Collections.emptyMap());
    }

    public Map<Long, Object> getEntityStorage(String entityType) {
        return getEntityStorageByNamespace(entityType);
    }

    public Long calculateNextId(String namespace) {
        // Retrieve the last ID from the storage and increment it for the next record
        Map<Long, Object> storage = entityStorage.get(namespace);
        if (storage != null && !storage.isEmpty()) {
            Long lastId = Collections.max(storage.keySet());
            return lastId + 1;
        } else {
            return 1L; // If no records exist, start with ID 1
        }
    }

    public void updateEntity(String entityData) {
        String[] parts = entityData.split("\\|");
        try {
            if (parts.length == 3) {
                String entityNamespace = parts[0];
                Long id = Long.parseLong(parts[1]);
                // Deserialize the entity based on the actual structure
                Object entity = deserializeEntity(entityNamespace, parts[2]);
                entityStorage.computeIfPresent(entityNamespace, (k, v) -> {
                    v.put(id, entity);
                    return v;
                });
            }
        } catch (StorageException | JsonProcessingException e) {
            log.error("Could not parse json {} with user type {}", parts[2], parts[0]);
        }
    }
}