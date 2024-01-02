package com.komponente.servis3.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EmailDto {
    private String email;
    private String subject;
    private String body;
    private String firstName;
    private String lastName;
    private String token;
    private String role;
    private Long userId;
    private String type;

    public EmailDto() {
    }
}
