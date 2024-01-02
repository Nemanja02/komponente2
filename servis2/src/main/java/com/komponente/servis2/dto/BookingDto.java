package com.komponente.servis2.dto;

import com.komponente.servis2.entity.TrainingSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
public class BookingDto {
    private Long id;
    private TrainingSession trainingSession;
    private Long clientId;
    private LocalDateTime bookedAt;
}
