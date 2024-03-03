package com.javacore.task.controllers;

import com.javacore.task.models.request.TrainingRequest;
import com.javacore.task.services.TrainingService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Timed
@PreAuthorize("hasAuthority('TRAINER')")
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Training API", description = "API for training operations")
public class TrainingController {

    private final TrainingService trainingService;
    private final Counter counter;

    @Operation(summary = "Save training")
    @PostMapping
    public ResponseEntity<String> saveTraining(@Valid @RequestBody TrainingRequest training) throws IOException {
        log.info("Endpoint called: POST /training, Request: {}", training);
        trainingService.saveTraining(training);
        log.info("Response: saved successfully");
        counter.increment();
        return new ResponseEntity<>("saved successfully", HttpStatus.OK);
    }

}