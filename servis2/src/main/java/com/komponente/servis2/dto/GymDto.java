package com.komponente.servis2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GymDto {
    private long id;
    private String name;
    private String description;
    private Long managerId;
    private int numberOfTrainers;
    private Iterable<TrainingTypeDto> trainingTypes;

    public GymDto() {
    }

    public GymDto(long id, String name, String description, Long managerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
    }
}
