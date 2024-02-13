package com.javacore.task.repositories;

import com.javacore.task.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {

    Optional<List<Training>> findByTraineeUserUsername(String username);
}
