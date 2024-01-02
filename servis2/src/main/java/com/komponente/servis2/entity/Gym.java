package com.komponente.servis2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long managerId;
    private Integer numberOfTrainers;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
            })
    @JoinTable(name = "gym_training_types",
            joinColumns = { @JoinColumn(name = "gym_id") },
            inverseJoinColumns = { @JoinColumn(name = "trainingType_id") })
    private Set<TrainingType> trainingTypes = new HashSet<>();
}
