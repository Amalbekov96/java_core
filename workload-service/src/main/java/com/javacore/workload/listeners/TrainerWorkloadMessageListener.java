package com.javacore.workload.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javacore.workload.DTO.request.TrainerWorkloadRequest;
import com.javacore.workload.model.MonthlySummary;
import com.javacore.workload.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadMessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SummaryService summaryService;

    @JmsListener(destination = "trainerWorkloadQueue")
    public void trainerMonthlySummary(String payloadJson) {

        objectMapper.registerModule(new JavaTimeModule());
        log.info("Received payload from the queue: {}", payloadJson);

        try {
            if (payloadJson == null) {
                log.error("Received null payload from the queue");
                return;
            }
           TrainerWorkloadRequest trainerWorkloadRequest = objectMapper.readValue(payloadJson, TrainerWorkloadRequest.class);
            log.info("Received TrainerWorkloadRequest from ActiveMQ: {}",trainerWorkloadRequest);
            summaryService.calculateSummary(trainerWorkloadRequest);
            MonthlySummary monthlySummary = summaryService.getSummaryByUsername(trainerWorkloadRequest.getUsername());
            log.info("trainerMonthlySummary returned {}", monthlySummary);
        } catch (Exception e) {
            log.error("Error deserializing TrainerWorkloadRequest", e);
        }
    }


}
