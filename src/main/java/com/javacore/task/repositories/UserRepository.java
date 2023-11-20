package com.javacore.task.repositories;

import com.javacore.task.entities.User;
import com.javacore.task.exceptions.StorageException;

import java.util.List;

public interface UserRepository {
    User findById(Long id);

    User save(User user) throws StorageException;

    void deleteById(Long id);

    List<User> findAll();
}
