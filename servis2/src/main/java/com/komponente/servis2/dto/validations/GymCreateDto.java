package com.komponente.servis2.dto.validations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GymCreateDto {
    private String name;
    private String description;
    private Long managerId;
    private int numberOfTrainers;
    private Iterable<Long> trainingTypes;

    public GymCreateDto() {
    }

    public GymCreateDto(String name, String description, Long managerId) {
        this.name = name;
        this.description = description;
        this.managerId = managerId;
    }
}
