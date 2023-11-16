package com.javacore.task.repositories;

import com.javacore.task.entities.Trainer;
import com.javacore.task.exceptions.StorageException;

import java.util.List;

public interface TrainerRepository {
    Trainer findById(Long id);

    Trainer save(Trainer trainer) throws StorageException;

    void deleteById(Long id);

    List<Trainer> findAll();
}
