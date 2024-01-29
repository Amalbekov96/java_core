package com.javacore.task.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@Builder
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

    @ManyToMany(cascade = {CascadeType.REMOVE,
            CascadeType.DETACH},mappedBy = "trainers",fetch = FetchType.EAGER)
    private List<Trainee> trainees;
}