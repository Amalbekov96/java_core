package com.javacore.task.repositories;

import com.javacore.task.entities.Trainee;
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
public interface TraineeRepository extends JpaRepository<Trainee,Long>  {
    @Query("select t from Trainee t where t.user.username = ?1")
    Optional<Trainee> findTraineeByUserUsername(String username);

    @Modifying
    @Query("update User u set u.password = :newPassword where u.userId in (select t.user.userId from Trainee t where t.traineeId = :id)")
    void changeTraineePassword(@Param("id") long id,@Param("newPassword") String newPassword);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_active = true WHERE id IN (SELECT t.user_id FROM trainee t WHERE t.id = ?1)")
    void activateTrainee(long id);


    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_active = false WHERE id IN (SELECT t.user_id FROM trainee t WHERE t.id = ?1)")
    void deactivateTrainee(long id);


    @Query("SELECT DISTINCT t FROM Trainer t " +

            "JOIN t.user u " +

            "JOIN t.specialization tt " +

            "WHERE t.id NOT IN (" +

            "   SELECT tr.id FROM Trainer tr " +

            "   JOIN tr.trainees trainee " +

            "   JOIN trainee.user u " +

            "   WHERE u.username = :username" +

            ")")
    List<Trainer> getNotAssignedTrainers(@Param("username") String username);


    @Query("SELECT t FROM Training t " +

            "WHERE t.trainee.user.username = :traineeUsername " +

            "AND t.trainingDate BETWEEN :periodFrom AND :periodTo " +

            "AND t.trainingType.trainingType = :trainingType " +

            "AND t.trainer.user.username = :trainerName " +

            "ORDER BY t.trainingDate ASC")
    List<Training> getTraineeTrainingsByCriteria(

            @Param("traineeUsername") String traineeUsername,

            @Param("periodFrom") Date periodFrom,

            @Param("periodTo") Date periodTo,

            @Param("trainerName") String trainerName,

            @Param("trainingType") String trainingType

    );


}
