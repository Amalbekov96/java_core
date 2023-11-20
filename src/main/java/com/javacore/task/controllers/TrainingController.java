package com.javacore.task.controllers;

import com.javacore.task.models.TrainingModel;
import com.javacore.task.services.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/{trainingId}")
    public ResponseEntity<TrainingModel> getTrainingById(@PathVariable Long trainingId) {
        TrainingModel trainingModel = trainingService.getTrainingById(trainingId);
        return ResponseEntity.ok(trainingModel);
    }

    @PostMapping
    public ResponseEntity<TrainingModel> createTraining(@RequestBody TrainingModel trainingModel) {
        TrainingModel createdTraining = trainingService.createTraining(trainingModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingModel> updateTraining(@PathVariable Long trainingId, @RequestBody TrainingModel trainingModel) {
        TrainingModel updatedTraining = trainingService.updateTraining(trainingId, trainingModel);
        return ResponseEntity.ok(updatedTraining);
    }

    @DeleteMapping("/{trainingId}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long trainingId) {
        trainingService.deleteTraining(trainingId);
        return ResponseEntity.noContent().build();
    }
}