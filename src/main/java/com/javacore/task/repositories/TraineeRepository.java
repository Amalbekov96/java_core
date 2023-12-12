package com.javacore.task.repositories;

import com.javacore.task.entities.Trainee;
import com.javacore.task.exceptions.StorageException;

import java.util.List;

public interface TraineeRepository {
    Trainee findById(Long id);

    Trainee save(Trainee trainee) throws StorageException;

    void deleteById(Long id);

    List<Trainee> findAll();
}
