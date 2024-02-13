package com.javacore.task.controllers;

import com.javacore.task.models.TrainerModel;
import com.javacore.task.models.request.TrainerTrainingsRequest;
import com.javacore.task.models.request.TrainerUpdateRequest;
import com.javacore.task.models.response.TrainerInfoResponse;
import com.javacore.task.models.response.TrainerTrainingInfoResponse;
import com.javacore.task.models.response.TrainerUpdateResponse;
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

    @PutMapping
    public ResponseEntity<TrainerUpdateResponse> updateTrainer(@Valid @RequestBody TrainerUpdateRequest request) {
        log.info("Endpoint called: PUT /trainers Request: {}", request);
        TrainerUpdateResponse updatedTrainer = trainerService.updateTrainer(request);
        log.info("Response: {}", updatedTrainer);
        return ResponseEntity.ok(updatedTrainer);
    }

    @GetMapping
    public ResponseEntity<TrainerInfoResponse> getTrainerProfileByName(@RequestParam("q") String username) {
        log.info("Endpoint called: GET /trainer, Request: q={}", username);
        TrainerInfoResponse response = trainerService.findTrainerProfileByUsername(username);
        log.info("Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> updateTrainerStatus(@RequestParam String username, @RequestParam("status") boolean status) {
        log.info("Endpoint called: PATCH /trainer?{}, Request: choice={}", username, status);
        String result = trainerService.updateTrainerStatus(status, username);
        log.info("Response: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainerTrainingInfoResponse>> getTrainerTrainingsList(@Valid @RequestBody TrainerTrainingsRequest request) {
        log.info("Endpoint called: POST /training/trainerTrainings, Request: {}", request);
        List<TrainerTrainingInfoResponse> responses = trainerService.getTrainerTrainingsByCriteria(request);
        log.info("Response: {}", responses);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}