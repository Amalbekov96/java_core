package com.javacore.task.service;

import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.Trainer;
import com.javacore.task.entities.Training;
import com.javacore.task.entities.User;
import com.javacore.task.enums.TrainingTypes;
import com.javacore.task.exceptions.UserNotFoundException;
import com.javacore.task.mappers.TraineeMapper;
import com.javacore.task.mappers.TrainingMapper;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.response.TraineeProfileUpdateResponse;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.request.TraineeUpdateRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TraineeTrainingInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.repositories.TrainerRepository;
import com.javacore.task.services.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void testGetTraineeById_ExistingTrainee() {

        long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setTraineeId(traineeId);
        when(traineeRepository.findById(traineeId)).thenReturn(java.util.Optional.of(trainee));
        TraineeModel expectedModel = new TraineeModel();
        when(traineeMapper.traineeToTraineeModel(trainee)).thenReturn(expectedModel);

        TraineeModel actualModel = traineeService.getTraineeById(traineeId);

        assertNotNull(actualModel);
    }

    @Test
    void testUpdateTrainee() {

        TraineeUpdateRequest request = new TraineeUpdateRequest(
                "Eldiyar.Toktomamatov","Erlan", "Artelev",new Date(),"Tokmok"   ,true);
        Trainee existingTrainee = new Trainee();
        when(traineeRepository.findTraineeByUserUsername(request.getUserName())).thenReturn(java.util.Optional.of(existingTrainee));
        Trainee updatedTrainee = new Trainee();
        when(traineeMapper.update(request, existingTrainee)).thenReturn(updatedTrainee);
        TraineeProfileUpdateResponse expectedResponse = new TraineeProfileUpdateResponse();
        when(traineeMapper.traineeToTraineeResponse(existingTrainee)).thenReturn(expectedResponse);
        TraineeProfileUpdateResponse actualResponse = traineeService.updateTrainee(request);

        assertThat(actualResponse).isNotNull();

    }

    @Test
    void testDeleteTrainee() {
        String username = "Eldiyar.Toktomamatov";
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> traineeService.deleteTrainee(username));

        verify(traineeRepository, times(1)).deleteById(trainee.getTraineeId());

    }

    @Test
    void testFindTraineeProfileByUsername_ExistingTrainee() {
        String username = "Kanysh.Abdyrakmanova";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUsername(username);
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));
        TraineeInfoResponse expectedResponse = new TraineeInfoResponse();
        when(traineeMapper.traineeInfoResponse(trainee)).thenReturn(expectedResponse);
        TraineeInfoResponse actualResponse = traineeService.findTraineeProfileByUsername(username);

        assertThat(actualResponse).isNotNull();

    }

    @Test
    void testUpdateTraineeStatus_ActivateTrainee() {
        String username = "Kanysh.Abdyrakmanova";
        boolean status = false;
        Trainee trainee = new Trainee();
        trainee.setTraineeId(2L);
        User user = new User();
        user.setIsActive(true);
        trainee.setUser(user);

        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));

        String result = traineeService.updateTraineeStatus(status, username);

        assertEquals("Deactivated", result);
        verify(traineeRepository, times(1)).deactivateTrainee(trainee.getTraineeId());
    }


    @Test
    void testUpdateTraineeTrainersList() {
        String username = "Kanysh.Abdyrakmanova";
        List<String> trainerUsernames = new ArrayList<>();
        Trainee trainee = new Trainee();
        trainee.setTraineeId(2L);
        trainee.setUser(new User());
        trainee.setTrainers(new ArrayList<>());
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));
        List<Trainer> trainers = new ArrayList<>();
        when(traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername())).thenReturn(trainers);
        List<Trainer> addedTrainers = new ArrayList<>();
        when(trainerRepository.findTrainersByUserUserName(trainerUsernames)).thenReturn(addedTrainers);

        List<TrainersListResponse> result = traineeService.updateTraineeTrainersList(username, trainerUsernames);

        verify(traineeRepository, times(1)).save(trainee);
        assertNotNull(result);
        assertEquals(0,result.size());
    }

    @Test
    void testGetNotAssignedActiveTrainersListForTrainee() {

        String username = "Kanysh.Abdyrakmanova";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));
        List<Trainer> trainers = new ArrayList<>();
        when(traineeRepository.getNotAssignedTrainers(trainee.getUser().getUsername())).thenReturn(trainers);

        TrainersListResponse trainer1 = new TrainersListResponse("Aiperi.Adylova", "Bektur", "Kanybek", "WEIGHT_LIFTING");
        TrainersListResponse trainer2 = new TrainersListResponse("Kushtar.Amalbekov", "Kushtar", "Amalbekov", "FITNESS");

        List<TrainersListResponse> expectedResponse = List.of(trainer1, trainer2);
        when(traineeMapper.mapTraineesTrainersToDto(trainers)).thenReturn(expectedResponse);

        List<TrainersListResponse> actualResponse = traineeService.getNotAssignedActiveTrainersListForTrainee(username);
        assertThat(actualResponse).isNotNull();
        assertEquals(expectedResponse, actualResponse);

    }
    @Test
    void testGetTraineeTrainingsByCriteria() {

        Date periodFrom = java.sql.Date.valueOf(LocalDate.of(2024, 2, 1));
        Date periodTo = java.sql.Date.valueOf(LocalDate.now());
        Date date = java.sql.Date.valueOf(LocalDate.of(2024, 2, 12));
        TraineeTrainingsRequest request = new TraineeTrainingsRequest(
                "Kairat.Uzenov","Aiperi.Adylova", TrainingTypes.WEIGHT_LIFTING.name(),periodFrom, periodTo);
        List<Training> trainings = new ArrayList<>();
        when(traineeRepository.getTraineeTrainingsByCriteria(
                anyString(), any(Date.class), any(Date.class), anyString(), any(TrainingTypes.class)
        )).thenReturn(Optional.of(trainings));
        TraineeTrainingInfoResponse response = new TraineeTrainingInfoResponse("thirdOne",date,"WEIGHT_LIFTING",8,"Kairat.Uzenov");
        List<TraineeTrainingInfoResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(response);
        when(trainingMapper.mapTraineeTrainingsToDto(trainings)).thenReturn(expectedResponse);

        List<TraineeTrainingInfoResponse> actualResponse = traineeService.getTraineeTrainingsByCriteria(request);

        verify(traineeRepository, times(1)).getTraineeTrainingsByCriteria(
                anyString(), any(Date.class), any(Date.class), anyString(), any(TrainingTypes.class)
        );
        verify(trainingMapper, times(1)).mapTraineeTrainingsToDto(trainings);
        assertEquals(expectedResponse, actualResponse);

    }


    @Test
    void testGetTraineeById_TraineeNotFound() {
        long traineeId = 1L;
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.getTraineeById(traineeId));
    }

    @Test
    void testUpdateTrainee_TraineeNotFound() {
        TraineeUpdateRequest request = new TraineeUpdateRequest(
                "Eldiyar.Toktomamatov","Erlan", "Artelev",new Date(),"Tokmok"   ,true);
        when(traineeRepository.findTraineeByUserUsername(request.getUserName())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.updateTrainee(request));
    }

    @Test
    void testDeleteTrainee_TraineeNotFound() {
        String username = "Eldiyar.Toktomamatov";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.deleteTrainee(username));
    }

    @Test
    void testFindTraineeProfileByUsername_TraineeNotFound() {
        String username = "Kanysh.Abdyrakmanova";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.findTraineeProfileByUsername(username));
    }

    @Test
    void testUpdateTraineeStatus_TraineeNotFound() {
        String username = "Kanysh.Abdyrakmanova";
        boolean status = false;
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.updateTraineeStatus(status, username));
    }

    @Test
    void testUpdateTraineeTrainersList_TraineeNotFound() {
        String username = "Kanysh.Abdyrakmanova";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.updateTraineeTrainersList(username, null));
    }

    @Test
    void testGetNotAssignedActiveTrainersListForTrainee_TraineeNotFound() {
        String username = "Kanysh.Abdyrakmanova";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeService.getNotAssignedActiveTrainersListForTrainee(username));
    }

    @Test
    void testGetTraineeTrainingsByCriteria_TraineeNotFound() {
        TraineeTrainingsRequest request = new TraineeTrainingsRequest(
                "Kairat.Uzenov","Aiperi.Adylova", "WEIGHT_LIFTING",null, null);
        when(traineeRepository.existsByUserUsername(request.getTraineeName())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> traineeService.getTraineeTrainingsByCriteria(request));
    }

}


