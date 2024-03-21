package com.javacore.task.mappers;

import com.javacore.task.entities.Training;
import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingMapper {

    private final TraineeMapper traineeMapper;

    private final TrainerMapper trainerMapper;

    private final TrainingTypeMapper trainingTypeMapper;

    public void update(TrainingModel trainingModel, Training existingTraining) {
        if (trainingModel != null && existingTraining != null) {
            existingTraining.setId(trainingModel.getId());
            existingTraining.setTrainee(traineeMapper.traineeModelToTrainee(trainingModel.getTrainee()));
            existingTraining.setTrainer(trainerMapper.trainerModelToTrainer(trainingModel.getTrainer()));
            existingTraining.setTrainingType(trainingTypeMapper.trainingTypeModelToTrainingType(trainingModel.getTrainingTypeModel()));
            existingTraining.setTrainingName(trainingModel.getTrainingName());
            existingTraining.setTrainingDate(trainingModel.getTrainingDate());
            existingTraining.setTrainingDuration(trainingModel.getTrainingDuration());
        }
    }
    public List<TraineeTrainingInfoResponse> mapTraineeTrainingsToDto(List<Training> trainings) {
        return trainings.stream()
                .map(this::mapTraineeTrainingToDto)
                .toList();
    }
    private TraineeTrainingInfoResponse mapTraineeTrainingToDto(Training training) {
        return new TraineeTrainingInfoResponse(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainer().getSpecialization() != null ? training.getTrainer().getSpecialization().getTrainingType().name() : null,
                training.getTrainingDuration(),
                training.getTrainee() != null ? training.getTrainee().getUser().getUsername() : null);
    }
    public List<TrainerTrainingInfoResponse> mapTrainerTrainingsToDto(List<Training> trainings) {
        return trainings.stream()
                .map(this::mapTrainerTrainingToDto)
                .toList();
    }
    private TrainerTrainingInfoResponse mapTrainerTrainingToDto(Training training) {
        return new TrainerTrainingInfoResponse(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainer().getSpecialization() != null ? training.getTrainer().getSpecialization().getTrainingType().name() : null,
                training.getTrainingDuration(),
                training.getTrainer().getUser().getUsername()
        );
    }


}