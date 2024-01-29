package com.javacore.task.repositories;

import com.javacore.task.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
}
