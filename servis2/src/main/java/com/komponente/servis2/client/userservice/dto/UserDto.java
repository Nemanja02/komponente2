package com.komponente.servis2.client.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate dateOfBirth;
    private String userType; // Admin, Client, Manager
    // CLIENT FIELDS
    private String membershipNumber;

    // MANAGER FIELDS
    private Long gymId;
    private LocalDate employmentDate;
    private String verified;
    private boolean active;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
