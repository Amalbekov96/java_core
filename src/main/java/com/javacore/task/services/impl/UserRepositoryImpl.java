package com.javacore.task.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacore.task.configs.InMemoryStorage;
import com.javacore.task.entities.User;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.repositories.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {

    private final InMemoryStorage inMemoryStorage;
    private final ObjectMapper objectMapper;

    @Override
    public User findById(Long id) {
        Map<Long, Object> userStorage = inMemoryStorage.getEntityStorage("User");
        if (userStorage != null) {
            return (User) userStorage.get(id);
        }
        return null;
    }

    @Override
    public User save(User user) throws StorageException {
        if (user.getUserId() == null) {
            // Calculate the next ID
            Long nextId = inMemoryStorage.calculateNextId("User");

            // Assign the calculated ID to the user entity
            user.setUserId(nextId);
            inMemoryStorage.addEntity("User|" + nextId + "|" + serializeEntity(user));
        } else {
            // Update the existing record in the storage
            inMemoryStorage.updateEntity("User|" + user.getUserId() + "|" + serializeEntity(user));
        }

        return user;
    }

    @Override
    public void deleteById(Long id) {
        Map<Long, Object> userStorage = inMemoryStorage.getEntityStorage("User");
        if (userStorage != null) {
            userStorage.remove(id);
        }
    }

    @Override
    public List<User> findAll() {
        Map<Long, Object> userStorage = inMemoryStorage.getEntityStorage("User");
        if (userStorage != null) {
            return new ArrayList<>(userStorage.values().stream()
                    .map(entity -> (User) entity)
                    .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    private String serializeEntity(User user) throws StorageException {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("Error serializing User entity", e);
            throw new StorageException("Error serializing User entity");
        }
    }
}
