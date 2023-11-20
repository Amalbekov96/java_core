package com.javacore.task.mappers;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.entities.TrainingType;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.TrainingTypeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {

    private final TraineeMapper traineeMapper;

    private final TrainerMapper trainerMapper;

    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingModel trainingToTrainingModel(Training training) {
        TrainingModel trainingModel = new TrainingModel();
        trainingModel.setId(training.getId());
        trainingModel.setTrainingName(training.getTrainingName());
        trainingModel.setTrainingDate(training.getTrainingDate());
        trainingModel.setTrainingDuration(training.getTrainingDuration());

        // Map Trainee using TraineeMapper
        if (training.getTrainee() != null) {
            TraineeModel traineeModel = traineeMapper.traineeToTraineeModel(training.getTrainee());
            trainingModel.setTrainee(traineeModel);
        }

        // Map Trainer using TrainerMapper
        if (training.getTrainer() != null) {
            TrainerModel trainerModel = trainerMapper.trainerToTrainerModel(training.getTrainer());
            trainingModel.setTrainer(trainerModel);
        }

        // Map TrainingType using TrainingTypeMapper
        if (training.getTrainingType() != null) {
            TrainingTypeModel trainingTypeModel = trainingTypeMapper.trainingTypeToTrainingTypeModel(training.getTrainingType());
            trainingModel.setTrainingTypeModel(trainingTypeModel);
        }

        return trainingModel;
    }

    public Training trainingModelToTraining(TrainingModel trainingModel) {
        Training training = new Training();
        training.setId(trainingModel.getId());
        training.setTrainingName(trainingModel.getTrainingName());
        training.setTrainingDate(trainingModel.getTrainingDate());
        training.setTrainingDuration(trainingModel.getTrainingDuration());

        // Map Trainee using TraineeMapper
        if (trainingModel.getTrainee() != null) {
            Trainee trainee = traineeMapper.traineeModelToTrainee(trainingModel.getTrainee());
            training.setTrainee(trainee);
        }

        // Map Trainer using TrainerMapper
        if (trainingModel.getTrainer() != null) {
            Trainer trainer = trainerMapper.trainerModelToTrainer(trainingModel.getTrainer());
            training.setTrainer(trainer);
        }

        // Map TrainingType using TrainingTypeMapper
        if (trainingModel.getTrainingTypeModel() != null) {
            TrainingType trainingType = trainingTypeMapper.trainingTypeModelToTrainingType(trainingModel.getTrainingTypeModel());
            training.setTrainingType(trainingType);
        }

        return training;
    }

    public void update(TrainingModel trainingModel, Training existingTraining) {
        if (trainingModel != null && existingTraining != null) {
            existingTraining.setId(trainingModel.getId());
            existingTraining.setTrainee(traineeMapper.traineeModelToTrainee(trainingModel.getTrainee()));
            existingTraining.setTrainer(trainerMapper.trainerModelToTrainer(trainingModel.getTrainer()));
            existingTraining.setTrainingType(trainingTypeMapper.trainingTypeModelToTrainingType(trainingModel.getTrainingTypeModel()));
            existingTraining.setTrainingName(trainingModel.getTrainingName());
            existingTraining.setTrainingDate(trainingModel.getTrainingDate());
            existingTraining.setTrainingDuration(trainingModel.getTrainingDuration());
            // Update other fields as needed
        }
    }

}