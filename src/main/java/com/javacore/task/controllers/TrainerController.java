package com.javacore.task.controllers;

import com.javacore.task.models.TrainerModel;
import com.javacore.task.services.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerModel> getTrainerById(@PathVariable Long trainerId) {
        TrainerModel trainerModel = trainerService.getTrainerById(trainerId);
        return ResponseEntity.ok(trainerModel);
    }

    @PostMapping
    public ResponseEntity<TrainerModel> createTrainer(@RequestBody TrainerModel trainerModel) {
        TrainerModel createdTrainer = trainerService.createTrainer(trainerModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainer);
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<TrainerModel> updateTrainer(@PathVariable Long trainerId, @RequestBody TrainerModel trainerModel) {
        TrainerModel updatedTrainer = trainerService.updateTrainer(trainerId, trainerModel);
        return ResponseEntity.ok(updatedTrainer);
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long trainerId) {
        trainerService.deleteTrainer(trainerId);
        return ResponseEntity.noContent().build();
    }
}