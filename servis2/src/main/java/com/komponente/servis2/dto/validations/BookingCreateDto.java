package com.komponente.servis2.dto.validations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class BookingCreateDto {
    private Long trainingSessionId;
    private Long clientId;

    public BookingCreateDto(Long trainingSessionId, Long clientId) {
        this.trainingSessionId = trainingSessionId;
        this.clientId = clientId;
    }

    public BookingCreateDto() {
    }
}
