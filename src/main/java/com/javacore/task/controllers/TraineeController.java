package com.javacore.task.controllers;

import com.javacore.task.models.TraineeModel;
import com.javacore.task.models.request.TraineeTrainingsRequest;
import com.javacore.task.models.response.TraineeInfoResponse;
import com.javacore.task.models.response.TrainersListResponse;
import com.javacore.task.models.response.TrainingInfoResponse;
import com.javacore.task.services.TraineeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('TRAINER')")
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/{traineeId}")
    public ResponseEntity<TraineeModel> getTraineeById(@PathVariable Long traineeId) {
        log.info("Endpoint called: GET /trainees/{}", traineeId);
        TraineeModel traineeModel = traineeService.getTraineeById(traineeId);
        log.info("Response: {}", traineeModel);
        return ResponseEntity.ok(traineeModel);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<TraineeModel> createTrainee(@RequestBody TraineeModel traineeModel) {
        log.info("Endpoint called: POST /trainees, Request: {}", traineeModel);
        TraineeModel createdTrainee = traineeService.createTrainee(traineeModel);
        log.info("Response: {}", createdTrainee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainee);
    }

    @PutMapping("/{traineeId}")
    public ResponseEntity<TraineeModel> updateTrainee(@PathVariable Long traineeId, @RequestBody TraineeModel traineeModel) {
        log.info("Endpoint called: PUT /trainees/{}", traineeId);
        TraineeModel updatedTrainee = traineeService.updateTrainee(traineeId, traineeModel);
        log.info("Response: {}", updatedTrainee);
        return ResponseEntity.ok(updatedTrainee);
    }

    @PostMapping("/{traineeId}")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<String> updateLogin(@PathVariable long traineeId, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {
        log.info("Endpoint called: POST /trainees/{}", traineeId);
        traineeService.changeTraineePassword(traineeId, password, newPassword);
        return new ResponseEntity<>("password successfully updated!", HttpStatus.OK);
    }
    @DeleteMapping("/{traineeId}")
    public ResponseEntity<String> deleteTrainee(@PathVariable("traineeId") long traineeId) {
        log.info("Endpoint called: DELETE /trainees/{}", traineeId);
        traineeService.deleteTrainee(traineeId);
        log.info("Response: deleted successfully");
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<TraineeInfoResponse> getTraineeProfile(@RequestParam("q") String username) {
        log.info("Endpoint called: GET /trainees, Request: q={}", username);
        TraineeInfoResponse traineeGetByNameResponse = traineeService.findTraineeProfileByUsername(username);
        log.info("Response: {}", traineeGetByNameResponse);
        return new ResponseEntity<>(traineeGetByNameResponse, HttpStatus.OK);
    }

    @PatchMapping("/{traineeId}")
    public ResponseEntity<String> updateTraineeStatus(@PathVariable("traineeId") long traineeId, @RequestParam("status") boolean status) {
        log.info("Endpoint called: PATCH /trainees/{}, Request: choice={}", traineeId, status);
        String result = traineeService.updateTraineeStatus(status, traineeId);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/not-assigned-trainers")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<List<TrainersListResponse>> getNotAssignedTrainersForTrainee(@RequestParam String username) {
        log.info("Endpoint called: GET /trainees/notAssignedTrainers, Request: username={}", username);
        List<TrainersListResponse> responses = traineeService.getNotAssignedActiveTrainersListForTrainee(username);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/update-trainers")
    public ResponseEntity<List<TrainersListResponse>> updateTraineeTrainersList(@RequestParam("username") String username, @RequestBody List<String> trainersUsernames) {
        log.info("Endpoint called: POST /trainees/updateList, Request: username={}, trainersUsernames={}", username, trainersUsernames);
        List<TrainersListResponse> responses = traineeService.updateTraineeTrainersList(username, trainersUsernames);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/trainee-trainings")
    @PreAuthorize("hasAuthority('TRAINEE')")
    public ResponseEntity<List<TrainingInfoResponse>> getTraineeTrainingsList(@Valid @RequestBody TraineeTrainingsRequest request) {
        log.info("Endpoint called: POST /training/traineeTrainings, Request: {}", request);
        List<TrainingInfoResponse> responses = traineeService.getTraineeTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}