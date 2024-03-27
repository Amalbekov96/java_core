package com.javacore.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.javacore.task.enums.TrainingTypes;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TrainingTypes trainingType;

    public TrainingType(TrainingTypes trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", trainingType=" + trainingType +
                '}';
    }
}