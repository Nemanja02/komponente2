package com.komponente.servis2.dto.validations;

import com.komponente.servis2.dto.BookingDto;
import com.komponente.servis2.dto.GymDto;
import com.komponente.servis2.dto.TrainingSessionDto;
import com.komponente.servis2.dto.TrainingTypeDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class TrainingSessionResponseDto {
    private Long id;
    private GymDto gym;
    private TrainingTypeResponseDto trainingType;
    private Integer startHour;
    private Integer startMinute;

    private Integer endHour;
    private Integer endMinute;
    private int dayOfWeek;
    private int maxParticipants;
    private boolean isCanceled;
    private Iterable<BookingDto> bookings;
}
