package com.komponente.servis2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
public class TrainingSessionDto {
    private Long id;
    private GymDto gym;
    private TrainingTypeDto trainingType;
    private Integer startHour;
    private Integer startMinute;

    private Integer endHour;
    private Integer endMinute;
    private int dayOfWeek;
    private int maxParticipants;
    private boolean isCanceled;

    public TrainingSessionDto() {
    }
}
