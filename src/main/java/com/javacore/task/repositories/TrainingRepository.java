package com.javacore.task.repositories;

import com.javacore.task.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {
    @Query("select t from Training t where t.trainer.id = ?1")
    List<Training> findByTrainerId(long id);
}
