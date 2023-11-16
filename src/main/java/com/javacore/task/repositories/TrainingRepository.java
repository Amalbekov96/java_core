package com.javacore.task.repositories;

import com.javacore.task.entities.Training;
import com.javacore.task.exceptions.StorageException;

import java.util.List;

public interface TrainingRepository {
    Training findById(Long id);

    Training save(Training training) throws StorageException;

    void deleteById(Long id);

    List<Training> findAll();
}
