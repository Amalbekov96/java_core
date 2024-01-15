package com.javacore.task.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    private Long id;
    private TrainingType specialization;
    private User user;
}