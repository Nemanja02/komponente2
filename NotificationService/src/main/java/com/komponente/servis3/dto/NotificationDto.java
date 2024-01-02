package com.komponente.servis3.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
public class NotificationDto {
    private String subject;
    private String body;
    private String type;
    private String role;
    private String userId;
    private LocalDateTime bookedAt;
}
