package com.komponente.servis2.dto.validations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TrainingTypeCreateDto {
    private String name;
    private boolean isGroup;

    public TrainingTypeCreateDto() {
    }

    public TrainingTypeCreateDto(String name, boolean isGroup) {
        this.name = name;
        this.isGroup = isGroup;
    }
}
