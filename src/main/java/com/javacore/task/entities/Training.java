package com.javacore.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TRAINEE_ID", referencedColumnName = "TRAINEE_ID")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", referencedColumnName = "ID")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "TRAINING_TYPE_ID", referencedColumnName = "ID")
    private TrainingType trainingType;

    @Column(name = "TRAINING_NAME", nullable = false)
    private String trainingName;

    @Column(name = "TRAINING_DATE", nullable = false)
    private Date trainingDate;

    @Column(name = "TRAINING_DURATION", nullable = false)
    private int trainingDuration;
}