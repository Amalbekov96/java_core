package com.javacore.task;


import com.javacore.task.entities.Trainee;
import com.javacore.task.entities.User;
import com.javacore.task.enums.UserType;
import com.javacore.task.exceptions.StorageException;
import com.javacore.task.mappers.TraineeMapper;
import com.javacore.task.mappers.TrainerMapper;
import com.javacore.task.mappers.UserMapper;
import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.UserModel;
import com.javacore.task.repositories.TraineeRepository;
import com.javacore.task.services.ProfileService;
import com.javacore.task.services.TrainerService;
import com.javacore.task.services.UserService;
import com.javacore.task.services.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @InjectMocks
    private TraineeServiceImpl traineeService;
    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TraineeMapper traineeMapper;
    @Test
    void getAll_shouldReturnAllTraineeModels() {

        User user = new User();
        user.setUserId(2L);
        user.setFirstName("John");
        user.setUsername("John.Doe");
        user.setIsActive(true);
        user.setPassword("asdafsdf");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUser(user);
        trainee.setAddress("Moscow");


        UserModel userModel = new UserModel();
        userModel.setUserId(2L);
        userModel.setFirstName("John");
        userModel.setUsername("John.Doe");
        userModel.setIsActive(true);
        userModel.setPassword("asdafsdf");

        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(1L);
        traineeModel.setUser(userModel);
        traineeModel.setAddress("Moscow");

        List<Trainee> trainees = new ArrayList<>();
        trainees.add(trainee);

        when(traineeRepository.findAll())
                .thenReturn(Collections.singletonList(trainee));

        when(traineeMapper.traineesToTraineeModels(trainees))
                .thenReturn(Collections.singletonList(traineeModel));

        List<TraineeModel> traineeModelList = traineeService.findAll();

        assertEquals(1, traineeModelList.size());
        verify(traineeRepository).findAll();
    }

    @Test
    void getById_withValidId_shouldReturnTraineeModel() {
        User user = new User();
        user.setUserId(2L);
        user.setFirstName("John");
        user.setUsername("John.Doe");
        user.setIsActive(true);
        user.setPassword("asdafsdf");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUser(user);
        trainee.setAddress("Moscow");


        UserModel userModel = new UserModel();
        userModel.setUserId(2L);
        userModel.setFirstName("John");
        userModel.setUsername("John.Doe");
        userModel.setIsActive(true);
        userModel.setPassword("asdafsdf");

        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(1L);
        traineeModel.setUser(userModel);
        traineeModel.setAddress("Moscow");

        when(traineeRepository.findById(1L))
                .thenReturn(trainee);
        when(traineeMapper.traineeToTraineeModel(trainee))
                .thenReturn(traineeModel);

        TraineeModel response = traineeService.getTraineeById(1L);
        assertEquals(traineeModel, response);
    }

    @Test
    void create_withValidData_shouldReturnTraineeModel() throws StorageException {

        User user = new User();
        user.setUserId(2L);
        user.setFirstName("John");
        user.setUsername("John.Doe");
        user.setIsActive(true);
        user.setPassword("asdafsdf");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUser(user);
        trainee.setAddress("Moscow");


        UserModel userModel = new UserModel();
        userModel.setUserId(2L);
        userModel.setFirstName("John");
        userModel.setUsername("John.Doe");
        userModel.setIsActive(true);
        userModel.setPassword("asdafsdf");

        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(1L);
        traineeModel.setUser(userModel);
        traineeModel.setAddress("Moscow");

        when(traineeRepository.save(any())).thenReturn(trainee);

        when(traineeMapper.traineeToTraineeModel(trainee))
                .thenReturn(traineeModel);

        TraineeModel response = traineeService.createTrainee(traineeModel);
        assertEquals(traineeModel, response);
    }

    @Test
    void update_withValidData_shouldReturnTraineeModel() {


        UserModel updateUserModel = new UserModel();
        updateUserModel.setUserId(1L);
        updateUserModel.setFirstName("John");
        updateUserModel.setUsername("John.Doe");
        updateUserModel.setIsActive(true);
        updateUserModel.setPassword("pass123");

        TraineeModel updateTraineeModel = new TraineeModel();
        updateTraineeModel.setTraineeId(2L);
        updateTraineeModel.setUser(updateUserModel);
        updateTraineeModel.setAddress("Bishkek");

        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setUsername("John.Doe");
        user.setIsActive(true);
        user.setPassword("pass123");

        Trainee trainee = new Trainee();
        trainee.setTraineeId(2L);
        trainee.setUser(user);
        trainee.setAddress("Bishkek");


        UserModel userModel = new UserModel();
        userModel.setUserId(1L);
        userModel.setFirstName("John");
        userModel.setUsername("John.Doe");
        userModel.setIsActive(true);
        userModel.setPassword("pass123");

        TraineeModel traineeModel = new TraineeModel();
        traineeModel.setTraineeId(2L);
        traineeModel.setUser(userModel);
        traineeModel.setAddress("Bishkek");

        when(traineeRepository.findById(traineeModel.getTraineeId()))
                .thenReturn(trainee);

        // Update the stubbing with any() matcher
        when(traineeMapper.traineeToTraineeModel(any())).thenReturn(updateTraineeModel);

        TraineeModel response = traineeService.updateTrainee(updateTraineeModel.getTraineeId(), updateTraineeModel);
        assertEquals(traineeModel, response);
    }

    @Test
    void deleteById_withValidId_shouldReturnVoid() {
        doNothing()
                .when(traineeRepository)
                .deleteById(1L);
        traineeService.deleteTrainee(1L);
        verify(traineeRepository).deleteById(1L);
    }
}