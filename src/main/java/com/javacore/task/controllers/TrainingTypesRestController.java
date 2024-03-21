package com.javacore.task.controllers;

import com.javacore.task.entities.TrainingType;
import com.javacore.task.services.TrainingTypeService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-type")
@Slf4j
@Timed
@PreAuthorize("hasAuthority('TRAINER')")
@RequiredArgsConstructor
@Tag(name = "Training Types Controller", description = "Endpoints for Training Types operations")
public class TrainingTypesRestController {

    private final TrainingTypeService trainingTypeService;
    private final Counter counter;

    @Operation(summary = "Get all training types")
    @Timed(value = "training-type.get", description = "Time taken to get all training types")
    @GetMapping
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
            log.info("Endpoint called: GET /training-type");
            List<TrainingType> trainingTypeList = trainingTypeService.getAllTrainingTypes();
            log.info("Response: Retrieved {} training types", trainingTypeList.size());
            counter.increment();
            return new ResponseEntity<>(trainingTypeList, HttpStatus.OK);
    }

}
