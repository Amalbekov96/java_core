package com.javacore.workload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "firstName_lastName_index", def = "{'firstName': 1, 'lastName': 1}")
})
public class MonthlySummary {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private List<Years> years;
    private AtomicInteger trainingSummarizedDuration;

}
