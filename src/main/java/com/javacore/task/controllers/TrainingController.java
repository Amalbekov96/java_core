package com.javacore.task.controllers;

import com.javacore.task.models.TrainingModel;
import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.services.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('TRAINER')")
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    public ResponseEntity<String> saveTraining(@Valid @RequestBody TrainingRequest training) throws IOException {
        log.info("Endpoint called: POST /training, Request: {}", training);
        trainingService.saveTraining(training);
        log.info("Response: saved successfully");
        return new ResponseEntity<>("saved successfully", HttpStatus.OK);
    }

}