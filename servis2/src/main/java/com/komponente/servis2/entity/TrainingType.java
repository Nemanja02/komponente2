package com.komponente.servis2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TrainingTypes")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isGroup;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
            })
    @JoinTable(name = "gym_training_types",
            joinColumns = { @JoinColumn(name = "trainingType_id") },
            inverseJoinColumns = { @JoinColumn(name = "gym_id") })
    @JsonIgnore
    private Set<Gym> gyms = new HashSet<>();

    public TrainingType(String name, boolean isGroup) {
        this.name = name;
        this.isGroup = isGroup;
    }

    public TrainingType() {
    }
}
