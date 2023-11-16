package com.javacore.task.mappers;

import com.javacore.task.entities.Trainee;
import com.javacore.task.models.TraineeModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TraineeMapper {

    TraineeModel traineeToTraineeModel(Trainee trainee);
    Trainee traineeModelToTrainee(TraineeModel traineeModel);
    void update(TraineeModel traineeModel,@MappingTarget Trainee trainee);
}