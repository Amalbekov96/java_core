package com.javacore.workload.controller;

import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.javacore.workload.DTO.request.TrainerWorkloadRequest;

import java.util.List;

@Slf4j
@EnableJms
@RestController
@RequestMapping("/trainer-workload")
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final SummaryService summaryService;
    @GetMapping
    public ResponseEntity<List<MonthlySummary>> getAllSummaries(){
        log.info("Endpoint called: GET /trainer-workload");
        return new ResponseEntity<>(summaryService.getAllSummaries(), HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<MonthlySummary> getSummaryByUsername(@RequestParam String username){
        log.info("Endpoint called: GET /trainer-workload/summary/{}", username);
        return new ResponseEntity<>(summaryService.getSummaryByUsername(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> updateSummary(TrainerWorkloadRequest request) {
        log.info("Endpoint called: POST /trainer-workload/{}", request);
        summaryService.updateSummary(summaryService.getSummaryByUsername(request.getUsername()).getId(), request);
        log.info("Response: updated successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSummary(@RequestParam String username) {
        log.info("Endpoint called: DELETE /trainer-workload/{}", username);
        summaryService.deleteSummaryByUsername(username);
        log.info("Response: deleted successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
