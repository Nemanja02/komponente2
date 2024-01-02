package com.komponente.servis2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TrainingTypeDto {
    private Long id;
    private String name;
    private boolean isGroup;

    public TrainingTypeDto() {
    }
}
