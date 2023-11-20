package com.javacore.task.controllers;

import com.javacore.task.models.TraineeModel;
import com.javacore.task.services.TraineeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/{traineeId}")
    public ResponseEntity<TraineeModel> getTraineeById(@PathVariable Long traineeId) {
        TraineeModel traineeModel = traineeService.getTraineeById(traineeId);
        return ResponseEntity.ok(traineeModel);
    }

    @PostMapping
    public ResponseEntity<TraineeModel> createTrainee(@RequestBody TraineeModel traineeModel) {
        TraineeModel createdTrainee = traineeService.createTrainee(traineeModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainee);
    }

    @PutMapping("/{traineeId}")
    public ResponseEntity<TraineeModel> updateTrainee(@PathVariable Long traineeId, @RequestBody TraineeModel traineeModel) {
        TraineeModel updatedTrainee = traineeService.updateTrainee(traineeId, traineeModel);
        return ResponseEntity.ok(updatedTrainee);
    }

    @DeleteMapping("/{traineeId}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable Long traineeId) {
        traineeService.deleteTrainee(traineeId);
        return ResponseEntity.noContent().build();
    }
}