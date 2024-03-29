package com.javacore.task.repositories;

import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    boolean existsByUserUsername(String username);
    @Query("select t from Trainer t where t.user.username = ?1")
    Optional<Trainer> findByUserUsername(String username);
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_active = true WHERE users.user_id IN (SELECT t.user_id FROM trainer t WHERE t.id = ?1)")
    void activateTrainer(long id);
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_active = false WHERE users.user_id IN (SELECT t.user_id FROM trainer t WHERE t.id = ?1)")
    void deactivateTrainer(long id);

    @Query("SELECT t FROM Training t " +
            "WHERE t.trainer.user.username = :trainerUsername " +
            "AND t.trainingDate BETWEEN :periodFrom AND :periodTo " +
            "AND t.trainee.user.username = :traineeName " +
            "ORDER BY t.trainingDate ASC")
    Optional<List<Training>> getTrainerTrainingsByCriteria(
            @Param("trainerUsername") String trainerUsername,
            @Param("periodFrom") Date periodFrom,
            @Param("periodTo")  Date periodTo,
            @Param("traineeName") String traineeName);
    @Query("SELECT t FROM Trainer t WHERE t.user.username IN :trainersUsernames")
    List<Trainer> findTrainersByUserUserName(@Param("trainersUsernames") List<String> trainersUsernames);
}
