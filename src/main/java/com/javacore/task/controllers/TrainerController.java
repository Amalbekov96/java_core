package com.javacore.task.controllers;

import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainingInfoResponse;
import com.javacore.task.services.TrainerService;
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
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerModel> getTrainerById(@PathVariable Long trainerId) {
        log.info("Endpoint called: GET /trainers/{}", trainerId);
        TrainerModel trainerModel = trainerService.getTrainerById(trainerId);
        log.info("Response: {}", trainerModel);
        return ResponseEntity.ok(trainerModel);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<TrainerModel> createTrainer(@RequestBody TrainerModel trainerModel) {
        log.info("Endpoint called: POST /trainers, Request: {}", trainerModel);
        TrainerModel createdTrainer = trainerService.createTrainer(trainerModel);
        log.info("Response: {}", createdTrainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainer);
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<TrainerModel> updateTrainer(@PathVariable Long trainerId, @RequestBody TrainerModel trainerModel) {
        log.info("Endpoint called: PUT /trainers/{}", trainerId);
        TrainerModel updatedTrainer = trainerService.updateTrainer(trainerId, trainerModel);
        log.info("Response: {}", updatedTrainer);
        return ResponseEntity.ok(updatedTrainer);
    }

    @PostMapping("/{trainerId}")
    public ResponseEntity<String> updateLogin(@PathVariable long trainerId, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {
        log.info("Endpoint called: POST /trainers/{}", trainerId);
        trainerService.changeTrainerPassword(trainerId, password, newPassword);
        return new ResponseEntity<>("password successfully updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<String> deleteTrainer(@PathVariable("trainerId") long trainerId) {
        log.info("Endpoint called: DELETE /trainer/{}", trainerId);
        trainerService.deleteTrainer(trainerId);
        log.info("Response: deleted successfully");
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<TrainerInfoResponse> getTrainerProfileByName(@RequestParam("q") String username) {
        log.info("Endpoint called: GET /trainer, Request: q={}", username);
        TrainerInfoResponse response = trainerService.findTrainerProfileByUsername(username);
        log.info("Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{trainerId}")
    public ResponseEntity<String> updateTrainerStatus(@PathVariable("trainerId") long trainerId, @RequestParam("status") boolean status) {
        log.info("Endpoint called: PATCH /trainer/{}, Request: choice={}", trainerId, status);
        String result = trainerService.updateTrainerStatus(status, trainerId);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainingInfoResponse>> getTrainerTrainingsList(@Valid @RequestBody TrainerTrainingsRequest request) {
        log.info("Endpoint called: POST /training/trainerTrainings, Request: {}", request);
        List<TrainingInfoResponse> responses = trainerService.getTrainerTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}