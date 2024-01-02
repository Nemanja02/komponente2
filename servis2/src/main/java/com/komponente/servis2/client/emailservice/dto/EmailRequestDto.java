package com.komponente.servis2.client.emailservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EmailRequestDto {
    private Long clientId;
    private Long managerId;

    public EmailRequestDto() {
    }

    public EmailRequestDto(Long clientId, Long managerId) {
        this.clientId = clientId;
        this.managerId = managerId;
    }
}
