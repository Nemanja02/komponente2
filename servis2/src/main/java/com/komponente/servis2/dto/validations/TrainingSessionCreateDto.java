package com.komponente.servis2.dto.validations;

import com.komponente.servis2.dto.GymDto;
import com.komponente.servis2.dto.TrainingTypeDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
public class TrainingSessionCreateDto {
    private Long gymId;
    private Long trainingTypeId;
    private Integer startHour;
    private Integer startMinute;

    private Integer endHour;
    private Integer endMinute;
    private int dayOfWeek;
    private int maxParticipants;

    public TrainingSessionCreateDto() {
    }
}
