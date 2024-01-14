package com.javacore.task.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.entities.*;
import com.javacore.task.exceptions.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryStorage {

    private final ObjectMapper objectMapper;

    private static final Map<String, Map<Long, Object>> entityStorage = new HashMap<>();

    @Value("${data.file.path}")
    private String dataFilePath;

    @PostConstruct
    public void initialize() throws IOException {
        if (dataFilePath != null && !dataFilePath.isEmpty()) {
            try (Stream<String> lines = Files.lines(Path.of(dataFilePath))) {
                lines.forEach(this::addEntity);
            }
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateFileContent, 0, 2, TimeUnit.MINUTES);
    }

    public void addEntity(String entityData) {
        // Assuming entity data is in the format "namespace,id,serializedEntity"
        String[] parts = entityData.split("\\|");
        try {
            if (parts.length == 3) {
                String entityNamespace = parts[0];
                Long id = Long.parseLong(parts[1]);
                // Deserialize the entity based on the actual structure
                Object entity = deserializeEntity(entityNamespace, parts[2]);
                entityStorage.computeIfAbsent(entityNamespace, k -> new HashMap<>()).put(id, entity);
            }
        } catch (StorageException | JsonProcessingException e) {
            e.printStackTrace();
            log.error("Could not parse json {} with user type {} due to {}", parts[2], parts[0], e.getMessage());
        }
    }

    private Object deserializeEntity(String entityNamespace, String serializedEntity) throws StorageException, JsonProcessingException {
        return switch (entityNamespace) {
            case "Trainer" -> objectMapper.readValue(serializedEntity, Trainer.class);
            case "Trainee" -> objectMapper.readValue(serializedEntity, Trainee.class);
            case "Training" -> objectMapper.readValue(serializedEntity, Training.class);
            case "User" -> objectMapper.readValue(serializedEntity, User.class);
            case "TrainingType" -> objectMapper.readValue(serializedEntity, TrainingType.class);
            default -> throw new StorageException("There is no type " + entityNamespace);
        };
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

                    v.remove(id);
                    deleteEntityFromFile(entityNamespace, id);
                    v.put(id, entity);
                    return v;
                });
            }
            updateFileContent();
        } catch (StorageException | JsonProcessingException e) {
            log.error("Could not parse json {} with user type {}", parts[2], parts[0]);
        }
    }

    public boolean usernameExists(String username, String userType) {
        Map<Long, Object> storage;
        storage = entityStorage.get(userType);

        if (storage != null && !storage.isEmpty()) {
            for (Object entity : storage.values()) {
                if (entity instanceof Trainer trainer) {
                    if (username.equals(trainer.getUser().getUsername())) {
                        return true;
                    }
                } else if (entity instanceof Trainee trainee) {
                    if (username.equals(trainee.getUser().getUsername())) {
                        return true;
                    }
                } else if (entity instanceof User user) {
                    if (username.equalsIgnoreCase(user.getUsername())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void updateFileContent() {
        log.info("START updating the file content");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dataFilePath))) {
            // Iterate through the entityStorage map and write each entry to the file
            for (Map.Entry<String, Map<Long, Object>> entry : entityStorage.entrySet()) {
                for (Map.Entry<Long, Object> innerEntry : entry.getValue().entrySet()) {
                    String line = entry.getKey() + "|" + innerEntry.getKey() + "|" +
                            objectMapper.writeValueAsString(innerEntry.getValue());
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
            e.printStackTrace();
        }
        log.info("FINISH updating the file content");
    }

    public void deleteEntityFromFile(String namespace, Long id) {
        try {
            List<String> updatedLines = Files.lines(Paths.get(dataFilePath))
                    .filter(line -> {
                        String[] parts = line.split("\\|");
                        return parts.length == 3 && parts[0].equals(namespace) && Long.parseLong(parts[1]) == id;
                    })
                    .collect(Collectors.toList());

            // Update the file content after removing the entity
            Files.write(Paths.get(dataFilePath), updatedLines);
            log.info("Entity with namespace '{}' and ID '{}' deleted successfully from the file.", namespace, id);
        } catch (IOException e) {
            log.error("Error deleting entity with namespace '{}' and ID '{}': {}", namespace, id, e.getMessage());
        }
    }

}