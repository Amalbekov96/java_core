package com.javacore.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SPECIALIZATION_ID", referencedColumnName = "ID")
    private TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;
}