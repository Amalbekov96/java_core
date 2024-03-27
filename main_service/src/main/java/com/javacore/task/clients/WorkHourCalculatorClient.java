package com.javacore.task.clients;

import com.javacore.task.models.CreateWorkHourCalculatorModel;
import com.javacore.task.models.WorkHourCalculatorModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "workload-service", url = "http://localhost:8083")
public interface WorkHourCalculatorClient {
    @PostMapping("/workload")
    WorkHourCalculatorModel create(@RequestBody CreateWorkHourCalculatorModel createWorkloadModel);

    @PostMapping("/workload/list")
    List<WorkHourCalculatorModel> create(@RequestBody List<CreateWorkHourCalculatorModel> createWorkloadModels);
}
