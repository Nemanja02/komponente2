package com.komponente.servis2.client.emailservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EmailTypeDto {
    private String email;
    private String subject;
    private String body;
    private String firstName;
    private String lastName;
    private String token;
    private String role;
    private String userId;
    private String type;


    public EmailTypeDto() {
    }
}
